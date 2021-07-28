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
package com.silverminer.shrines.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * @author Silverminer
 *
 */
public class StructureUtils {
	protected static final Logger LOGGER = LogManager.getLogger(StructureUtils.class);

	public static IStructureConfig getConfigOf(String structure, boolean onServer) {
		for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
			if (holder.getStructure().getConfig().getName().equals(structure)) {
				return holder.getStructure().getConfig();
			}
		}
		return Utils.getData(structure, onServer);
	}

	public static boolean checkBiome(List<? extends Object> allowedBiomeCategories,
			List<? extends String> blacklistedBiomes, ResourceLocation name, Biome.Category category) {
		boolean flag = allowedBiomeCategories.contains(category.toString())
				|| allowedBiomeCategories.contains(category);

		if (!blacklistedBiomes.isEmpty() && flag) {
			flag = !blacklistedBiomes.contains(name.toString());
		}

		return flag;
	}

	public static void setupWorldGen() {
		if (Config.SETTINGS.ADVANCED_LOGGING.get())
			LOGGER.info("Generating {} Structures", NewStructureInit.STRUCTURES.size());
		for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
			ShrinesStructure structure = holder.getStructure();
			holder.configure();

			StructureSeparationSettings structureSeparationSettings = new StructureSeparationSettings(
					structure.getDistance(), structure.getSeparation(), structure.getSeedModifier());

			DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
					.putAll(DimensionStructuresSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

			WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
				Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings()
						.structureConfig();
				if (structureMap instanceof ImmutableMap) {
					Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
					tempMap.put(structure, structureSeparationSettings);
					settings.getValue().structureSettings().structureConfig = tempMap;
				} else {
					structureMap.put(structure, structureSeparationSettings);
				}
			});

			WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
					structure.getRegistryName().toString(), holder.getConfiguredStructure());
			FlatGenerationSettings.STRUCTURE_FEATURES.put(structure, holder.getConfiguredStructure());
		}
	}

	private static Method GETCODEC_METHOD;
	@SuppressWarnings("resource")
	public static void addDimensionalSpacing(ServerWorld world) {

		/*
		 * Skip Terraforged's chunk generator as they are a special case of a mod
		 * locking down their chunkgenerator. They will handle your structure spacing
		 * for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your
		 * structure's registration.
		 */
		try {
			if (GETCODEC_METHOD == null)
				GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
			@SuppressWarnings("unchecked")
			// cgRL = chunk generator Resource Location
			ResourceLocation cgRL = Registry.CHUNK_GENERATOR
					.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(world.getChunkSource().generator));
			if (cgRL != null && cgRL.getNamespace().equals("terraforged"))
				return;
		} catch (Exception e) {
			LOGGER.error("Was unable to check if " + world.dimension().location()
					+ " is using Terraforged's ChunkGenerator.");
		}

		Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(
				world.getChunkSource().generator.getSettings().structureConfig());
		if (world.getChunkSource().getGenerator() instanceof FlatChunkGenerator
				&& world.dimension().equals(World.OVERWORLD)) {
			tempMap.keySet().removeAll(NewStructureInit.STRUCTURES.stream().map(holder -> holder.getStructure())
					.collect(Collectors.toList()));
		} else {
			for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
				if (isAllowedForWorld(world, holder.getStructure().getConfig())) {
					tempMap.putIfAbsent(holder.getStructure(), DimensionStructuresSettings.DEFAULTS.get(holder.getStructure()));
				} else {
					tempMap.remove(holder.getStructure());
				}
			}
		}
		world.getChunkSource().generator.getSettings().structureConfig = tempMap;
	}

	public static boolean isAllowedForWorld(ISeedReader currentWorld, IStructureConfig config) {
		//return true;
		String worldID = currentWorld.getLevel().dimension().location().toString();
		return config.getDimensions().contains(worldID);
	}
}