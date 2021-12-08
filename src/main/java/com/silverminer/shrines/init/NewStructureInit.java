/**
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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Silverminer
 */
@EventBusSubscriber(modid = ShrinesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NewStructureInit {
    protected static final Logger LOGGER = LogManager.getLogger(NewStructureInit.class);
    public static final ImmutableList<StructureRegistryHolder> STRUCTURES = ImmutableList
            .<StructureRegistryHolder>builder().addAll(initStructures()).build();

    private static ArrayList<StructureRegistryHolder> initStructures() {
        StructureLoadUtils.FINAL_STRUCTURES_PACKETS = ImmutableList.copyOf
                (StructureLoadUtils.getStructurePackets());
        ArrayList<StructureRegistryHolder> structures = Lists.newArrayList();
        LOGGER.info("Registering shrines structures");
        for (StructuresPacket packet : StructureLoadUtils.FINAL_STRUCTURES_PACKETS) {
            for (StructureData structure : packet.getStructures()) {
                if (structure.successful) {
                    String name = structure.getKey();

                    structures.add(new StructureRegistryHolder(name, structure));
                    structure.registered = true;
                } else {
                    structure.registered = false;
                }
            }
        }
        return structures;
    }

    @SubscribeEvent
    public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        LOGGER.info("Registering {} structures of shrines Mod", STRUCTURES.size());
        for (StructureRegistryHolder holder : STRUCTURES) {
            StructureFeature.STRUCTURES_REGISTRY.putIfAbsent(holder.getStructure().getConfig().getKey(),
                    holder.getStructure());

            if (holder.getStructure().getConfig().getTransformLand()) {
                StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder()
                        .addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(holder.getStructure()).build();
            }

            ShrinesStructure structure = holder.getStructure();

            StructureFeatureConfiguration structureSeparationSettings = new StructureFeatureConfiguration(
                    structure.getDistance(), structure.getSeparation(), structure.getSeedModifier());

            StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                    .putAll(StructureSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

            BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
                Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings()
                        .structureConfig();
                if (structureMap instanceof ImmutableMap) {
                    Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                    tempMap.put(structure, structureSeparationSettings);
                    settings.getValue().structureSettings().structureConfig = tempMap;
                } else {
                    structureMap.put(structure, structureSeparationSettings);
                }
            });

            event.getRegistry().register(structure);
        }
    }
}