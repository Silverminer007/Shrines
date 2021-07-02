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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class AbstractStructure extends JigsawStructure {
	protected static final Logger LOGGER = LogManager.getLogger(AbstractStructure.class);
	@Nullable
	private StructureFeature<VillageConfig, ? extends Structure<VillageConfig>> configured = null;

	public final String name;
	public IStructureConfig structureConfig;

	public AbstractStructure(Codec<VillageConfig> codec, String nameIn, IStructureConfig config) {
		this(codec, 0, nameIn, config);
	}

	public AbstractStructure(Codec<VillageConfig> codec, int startY, String nameIn, IStructureConfig config) {
		super(codec, startY, true, true);
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
			if (Config.SETTINGS.ADVANCED_LOGGING.get())
				LOGGER.info("Building Config");
			this.structureConfig = ((ConfigBuilder) this.structureConfig).build(BUILDER);
		}
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
			int chunkX, int chunkZ, Biome biome, ChunkPos pos, VillageConfig config) {

		// Check the entire size of the structure to see if it's all a viable biome:
		for (Biome biome1 : provider.getBiomesWithin(chunkX * 16 + 9, generator.getSeaLevel(), chunkZ * 16 + 9, 50)) {
			if (!biome1.getGenerationSettings().isValidStart(this)) {
				return false;
			}
		}

		rand.setLargeFeatureSeed(seed, chunkX, chunkZ);
		List<Structure<?>> structures = new ArrayList<>();

		for (AbstractStructure iStructure : NewStructureInit.STRUCTURES.values()) {
			Structure<?> structure = iStructure.getStructure();

			if (structure.step() == this.step()) {
				structures.add(structure);
			}
		}

		structures.add(Structure.VILLAGE);
		if (!this.checkForOtherStructures(this, generator, seed, rand, chunkX, chunkZ, structures)) {
			return false;
		}
		return rand.nextDouble() < getSpawnChance();
	}

	protected boolean checkForOtherStructures(Structure<?> structure, ChunkGenerator generator, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, List<Structure<?>> structures) {
		for (Structure<?> structure1 : structures) {
			StructureSeparationSettings separationSettings = generator.getSettings().getConfig(structure1);

			if (separationSettings == null || structure == structure1) {
				continue;
			}

			int distance = Config.SETTINGS.STRUCTURE_MIN_DISTANCE.get();
			for (int x = chunkX - distance; x <= chunkX + distance; x++) {
				for (int z = chunkZ - distance; z <= chunkZ + distance; z++) {
					ChunkPos structurePos = structure1.getPotentialFeatureChunk(separationSettings, seed, rand, x, z);

					if (x == structurePos.x && z == structurePos.z) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public StructureFeature<VillageConfig, ? extends Structure<VillageConfig>> getConfigured() {
		if (this.configured == null) {
			this.configured = this.configured(new VillageConfig(() -> this.getPools(), 7));
		}
		return this.configured;
	}
}