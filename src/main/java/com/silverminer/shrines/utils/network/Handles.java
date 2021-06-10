/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.utils.network;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.client.gui.config.resource.AddResourceScreen;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author Silverminer
 *
 */
public class Handles {
	protected static final Logger LOGGER = LogManager.getLogger(Handles.class);

	public static class HandleCustomStructures {
		public static void handleCustomStructuresClient(SCustomStructuresPacket pkt,
				Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> handleClient(pkt.datas)));
			context.setPacketHandled(true);
		}

		public static DistExecutor.SafeRunnable handleClient(ArrayList<CustomStructureData> datas) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Utils.setStructures(datas, false);
					LOGGER.info("Recived structures from server {}\n{}", datas, Utils.getData("house3", false).PIECES_ON_FLY);
				}
			};
		}

		public static void handleCustomStructuresServer(CCustomStructuresPacket pkt,
				Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(handleServer(pkt.datas));
			context.setPacketHandled(true);
		}

		public static DistExecutor.SafeRunnable handleServer(ArrayList<CustomStructureData> datas) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Utils.setStructures(datas, true);
					LOGGER.info("Recived structures from client {}", datas);
				}
			};
		}

		public static void handleSaveCS(CSaveCustomStructuresPacket pkt,
				Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(handleSave(pkt.structure, pkt.author, pkt.entities, pkt.dimension));
			context.setPacketHandled(true);
		}

		public static DistExecutor.SafeRunnable handleSave(String structure, String author, boolean entities,
				ResourceLocation dimension) {
			return new DistExecutor.SafeRunnable() {
				private static final long serialVersionUID = 2755718549913023560L;

				@Override
				public void run() {
					LOGGER.info("Save running");
					try {
						MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
						ServerWorld world = server.getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, dimension));
						CustomStructureData data = Utils.getData(structure, true);
						if (data.savePieces(world, server, author, entities)) {
							data.addBounds();
						}
						Utils.replace(data, true);
						LOGGER.info("Save Done");
						ShrinesPacketHandler.sendToServer(new SSaveCSDonePacket());
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			};
		}

		public static void handleSaveCSDone(SSaveCSDonePacket pkt, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> handleSaveDone()));
			context.setPacketHandled(true);
		}

		public static DistExecutor.SafeRunnable handleSaveDone() {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					if (AddResourceScreen.isInPiecesScreen != null) {
						AddResourceScreen.isInPiecesScreen.onSaveComplete();
					}
				}
			};
		}
	}
}