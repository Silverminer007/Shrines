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

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.custom.helper.ResourceData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class PiecesToDrawPacket {
	protected static final Logger LOGGER = LogManager.getLogger(PiecesToDrawPacket.class);

	private final ArrayList<ResourceData> bounds;
	private final String structure;

	public PiecesToDrawPacket(ArrayList<ResourceData> bounds, String structure) {
		this.bounds = bounds;
		this.structure = structure;
	}

	public static void encode(PiecesToDrawPacket pkt, PacketBuffer buf) {
		buf.writeUtf(pkt.structure);
		buf.writeInt(pkt.bounds.size());
		CompoundNBT tag = new CompoundNBT();
		int i = 0;
		for (ResourceData rd : pkt.bounds) {
			CompoundNBT nbt = new CompoundNBT();
			MutableBoundingBox mbb = rd.getBounds();
			nbt.putInt("x0", mbb.x0);
			nbt.putInt("y0", mbb.y0);
			nbt.putInt("z0", mbb.z0);
			nbt.putInt("x1", mbb.x1);
			nbt.putInt("y1", mbb.y1);
			nbt.putInt("z1", mbb.z1);
			nbt.putString("piece", rd.getName());
			tag.put(String.valueOf(i++), nbt);
		}
		buf.writeNbt(tag);
	}

	public static PiecesToDrawPacket decode(PacketBuffer buf) {
		String structure = buf.readUtf();
		ArrayList<ResourceData> bounds = Lists.newArrayList();
		int i = buf.readInt();
		CompoundNBT nbt = buf.readNbt();
		for (int j = 0; j < i; j++) {
			CompoundNBT tag = nbt.getCompound(String.valueOf(j));
			bounds.add(new ResourceData(tag.getString("piece"), MutableBoundingBox.createProper(tag.getInt("x0"),
					tag.getInt("y0"), tag.getInt("z0"), tag.getInt("x1"), tag.getInt("y1"), tag.getInt("z1"))));
		}
		return new PiecesToDrawPacket(bounds, structure);
	}

	public static void handle(PiecesToDrawPacket pkt, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(
				() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handleServer(pkt.bounds, pkt.structure)));
		context.setPacketHandled(true);
	}

	public static class Handle {

		public static DistExecutor.SafeRunnable handleServer(ArrayList<ResourceData> bounds, String structure) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Utils.getData(structure, false);
					LOGGER.info("Recived bounds from server {}", bounds);
				}
			};
		}
	}
}