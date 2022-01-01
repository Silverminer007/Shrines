/*
 * Silverminer (and Team)
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * <p>
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * @author Silverminer
 */
@EventBusSubscriber(modid = ShrinesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NewStructureInit {
   protected static final Logger LOGGER = LogManager.getLogger(NewStructureInit.class);
   public static final ImmutableList<StructureRegistryHolder> STRUCTURES = ImmutableList
         .<StructureRegistryHolder>builder().addAll(initStructures()).build();

   private static ArrayList<StructureRegistryHolder> initStructures() {
      PackageManagerProvider.SERVER.bootstrapPackages();
      ArrayList<StructureRegistryHolder> structures = Lists.newArrayList();
      for (StructuresPackageWrapper packet : PackageManagerProvider.SERVER.getInitialStructurePackages().getAsIterable()) {
         for (StructureData structure : packet.getStructures().getAsIterable()) {
            structures.add(new StructureRegistryHolder(structure.getKey(), structure));
         }
      }
      return structures;
   }

   @SubscribeEvent
   public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
      LOGGER.info("Registering {} structures of shrines Mod", STRUCTURES.size());
      for (StructureRegistryHolder holder : STRUCTURES) {
         event.getRegistry().register(holder.getStructure());
      }
   }
}