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
package com.silverminer.shrines.structures;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.init.NewStructureInit;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class AbstractStructure extends JigsawStructure {
	protected static final Logger LOGGER = LogManager.getLogger(AbstractStructure.class);

	public final String name;
	public IStructureConfig structureConfig;

	public AbstractStructure(Codec<VillageConfig> codec, String nameIn, IStructureConfig config) {
		this(codec, 0, nameIn, config);
	}

	public AbstractStructure(Codec<VillageConfig> codec, int startY, String nameIn, IStructureConfig config) {
		super(codec, startY, false, true);
		this.name = nameIn;
		this.structureConfig = config;
		this.setRegistryName(this.getFeatureName());
	}

	public abstract JigsawPattern getPools();

	@Override
	public String getFeatureName() {
		return new ResourceLocation(ShrinesMod.MODID, this.name).toString();
	}

	public int getDistance() {
		return (int) (this.getConfig().getDistance() * Config.SETTINGS.DISTANCE_FACTOR.get());
	}

	public int getSeparation() {
		return (int) (this.getConfig().getSeparation() * Config.SETTINGS.SEPERATION_FACTOR.get());
	}

	public int getSeedModifier() {
		return this.getConfig().getSeed();
	}

	public double getSpawnChance() {
		return this.getConfig().getSpawnChance();
	}

	public boolean needsGround() {
		return this.getConfig().getNeedsGround();
	}

	public List<? extends String> getDimensions() {
		return this.getConfig().getDimensions();
	}

	public IStructureConfig getConfig() {
		return this.structureConfig;
	}

	public void buildConfig(final ForgeConfigSpec.Builder BUILDER) {
		if (this.structureConfig instanceof ConfigBuilder) {
			LOGGER.info("Building Config");
			this.structureConfig = ((ConfigBuilder) this.structureConfig).build(BUILDER);
		}
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
			int chunkX, int chunkZ, Biome biome, ChunkPos pos, VillageConfig config) {
		return this.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config);
	}

	public boolean validateGeneration(ChunkGenerator generator, BiomeProvider provider, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
			@Nullable Structure<?>... exeptStructure) {

		// Check the entire size of the structure to see if it's all a viable biome:
		for (Biome biome1 : provider.getBiomesWithin(chunkX * 16 + 9, generator.getSeaLevel(), chunkZ * 16 + 9, 50)) {
			if (!biome1.getGenerationSettings().isValidStart(this)) {
				return false;
			}
		}

		rand.setLargeFeatureSeed(seed, chunkX, chunkZ);
		return rand.nextDouble() < getSpawnChance();
	}

	protected boolean checkForOtherStructures(ChunkGenerator generator, BiomeProvider provider, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
			@Nullable Structure<?>... exeptStructure) {
		for (AbstractStructure s : NewStructureInit.STRUCTURES.values()) {
			if (exeptStructure != null)
				for (Structure<?> es : exeptStructure) {
					if (es.equals(s))
						continue;
				}
			if (new ChunkPos(chunkX, chunkZ).equals(s.getPotentialFeatureChunk(
					new StructureSeparationSettings(s.getDistance(), s.getSeparation(), s.getSeedModifier()), seed,
					rand, chunkX, chunkZ))
					&& s.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config,
							Lists.asList(s, exeptStructure).toArray(new Structure<?>[exeptStructure.length]))) {
				return false;
			}
		}
		return true;
	}
}