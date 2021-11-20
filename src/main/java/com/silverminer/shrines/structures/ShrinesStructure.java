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
package com.silverminer.shrines.structures;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.structures.load.StructureData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class ShrinesStructure extends Structure<NoFeatureConfig> {
    protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);

    public final String name;
    public StructureData structureConfig;

    public ShrinesStructure(String nameIn, StructureData config) {
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
        return this.name;
    }

    public int getDistance() {
        return (int) (this.getConfig().getDistance() * Config.SETTINGS.DISTANCE_FACTOR.get());
    }

    public int getSeparation() {
        return (int) (this.getConfig().getSeperation() * Config.SETTINGS.SEPERATION_FACTOR.get());
    }

    public int getSeedModifier() {
        return this.getConfig().getSeed_modifier();
    }

    public double getSpawnChance() {
        return this.getConfig().getSpawn_chance();
    }

    public List<? extends String> getDimensions() {
        return this.getConfig().getDimension_whitelist();
    }

    public StructureData getConfig() {
        return this.structureConfig;
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
        if (generator.getFirstFreeHeight(chunkX, chunkZ, Heightmap.Type.WORLD_SURFACE_WG) < 60
                && biome.getBiomeCategory() != Biome.Category.NETHER) {
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
        return (structure, chunkX, chunkZ, mbb, references, seed) -> new Start(this, chunkX, chunkZ, mbb, references, seed);
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

            String location = this.feature.structureConfig.getStart_pool();
            LocalDate localdate = LocalDate.now();
            if (this.feature.getFeatureName().contains("balloon") && localdate.get(ChronoField.MONTH_OF_YEAR) == 6) {
                location += "_rainbow";
            }
            ResourceLocation pool = new ResourceLocation(location);
            VillageConfig config = new VillageConfig(
                    () -> registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(pool),
                    this.feature.getMaxDepth());

            BlockPos blockpos;
            // Check if the actual Dimension has ceiling. If so we don't want it to be
            // generated over it so we need to search for another place
            if (biome.getBiomeCategory().equals(Biome.Category.NETHER)) {
                blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
                IBlockReader blockReader = chunkGenerator.getBaseColumn(blockpos.getX(), blockpos.getZ());
                while (!blockReader.getBlockState(blockpos).isAir(blockReader, blockpos)) {
                    blockpos = blockpos.above();
                }
                JigsawManager.addPieces(registries, config, AbstractVillagePiece::new, chunkGenerator, templateManager,
                        blockpos, this.pieces, this.random, false, false);
            } else {
                blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
                JigsawManager.addPieces(registries, config, AbstractVillagePiece::new, chunkGenerator, templateManager,
                        blockpos, this.pieces, this.random, false, true);
            }

            Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = blockpos.getX() - structureCenter.getX();
            int zOffset = blockpos.getZ() - structureCenter.getZ();
            for (StructurePiece structurePiece : this.pieces) {
                structurePiece.move(xOffset, 0, zOffset);
            }

            this.calculateBoundingBox();
        }
    }
}