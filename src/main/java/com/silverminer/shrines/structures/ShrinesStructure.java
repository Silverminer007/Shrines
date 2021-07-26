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

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.ForgeConfigSpec;

public class ShrinesStructure extends Structure<NoFeatureConfig> {
	protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);

	public final String name;
	public IStructureConfig structureConfig;

	public ShrinesStructure(String nameIn, IStructureConfig config) {
		super(NoFeatureConfig.CODEC);
		this.name = nameIn;
		this.structureConfig = config;
		this.setRegistryName(this.getFeatureName());
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

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

	public List<? extends String> getDimensions() {
		return this.getConfig().getDimensions();
	}

	public IStructureConfig getConfig() {
		return this.structureConfig;
	}

	public void buildConfig(final ForgeConfigSpec.Builder BUILDER) {
		if (this.structureConfig instanceof ConfigBuilder) {
			this.structureConfig = ((ConfigBuilder) this.structureConfig).build(BUILDER);
		}
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
			int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config) {
		rand.setLargeFeatureSeed(seed, chunkX, chunkZ);
		List<Structure<?>> structures = new ArrayList<>();

		for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
			Structure<?> structure = holder.getStructure();

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

	public int getMaxDepth() {
		return 7;
	}

	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return (structure, chunkX, chunkZ, mbb, references, seed) -> {
			return new ShrinesStructure.Start(this, chunkX, chunkZ, mbb, references, seed);
		};
	}

	public static class Start extends MarginedStructureStart<NoFeatureConfig> {
		private final ShrinesStructure feature;

		public Start(ShrinesStructure structure, int chunkX, int chunkZ, MutableBoundingBox mbb, int references,
				long seed) {
			super(structure, chunkX, chunkZ, mbb, references, seed);
			this.feature = structure;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig no_config) {
			JigsawPatternRegistry.bootstrap();

			String location = this.feature.getFeatureName() + "/start_pool";
			LocalDate localdate = LocalDate.now();
			if (this.feature.getFeatureName().contains("balloon") && localdate.get(ChronoField.MONTH_OF_YEAR) == 6) {
				location += "rainbow";
			}
			ResourceLocation pool = new ResourceLocation(location);
			VillageConfig config = new VillageConfig(
					() -> registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(pool),
					this.feature.getMaxDepth());

			// Check if the actual Dimension has ceiling. If so we don't want it to be
			// generated over it so we need to search for another place
			if (biome.getBiomeCategory().equals(Biome.Category.NETHER)) {
				BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
				IBlockReader blockReader = chunkGenerator.getBaseColumn(blockpos.getX(), blockpos.getZ());
				while (!blockReader.getBlockState(blockpos).isAir(blockReader, blockpos)) {
					blockpos = blockpos.above();
				}
				JigsawManager.addPieces(registries, config, AbstractVillagePiece::new, chunkGenerator, templateManager,
						blockpos, this.pieces, this.random, false, false);
			} else {
				BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
				JigsawManager.addPieces(registries, config, AbstractVillagePiece::new, chunkGenerator, templateManager,
						blockpos, this.pieces, this.random, false, true);
			}

			this.calculateBoundingBox();
		}
	}
}