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
package com.silverminer.shrines.forge.utils.network;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.core.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.core.utils.custom_structures.Utils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author Silverminer
 *
 */
public class Handles {
	protected static final Logger LOGGER = LogManager.getLogger(Handles.class);

	public static class HandleCustomStructures {
		public static void handleCustomStructures(CustomStructuresPacket pkt,
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
					Utils.DATAS_FROM_SERVER = datas;
					LOGGER.info("Recived structures from server {}", datas);
				}
			};
		}
	}
}