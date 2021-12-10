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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShrinesStructure extends NoiseAffectingStructureFeature<JigsawConfiguration> {
   protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);

   public final String name;
   public StructureData structureConfig;

   public ShrinesStructure(String nameIn, StructureData config) {
      super(JigsawConfiguration.CODEC, (context) -> {
         if (!ShrinesStructure.checkLocation(context, config)) {
            return Optional.empty();
         } else {
            JigsawConfiguration newConfig = new JigsawConfiguration(
                  () -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(new ResourceLocation(config.getStart_pool())),
                  7
            );
            PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                  context.chunkGenerator(),
                  context.biomeSource(),
                  context.seed(),
                  context.chunkPos(),
                  newConfig,
                  context.heightAccessor(),
                  context.validBiome(),
                  context.structureManager(),
                  context.registryAccess()
            );
            Pools.bootstrap();

            BlockPos position = new BlockPos(SectionPos.sectionToBlockCoord(context.chunkPos().x), 0, SectionPos.sectionToBlockCoord(context.chunkPos().z));
            if (context.chunkGenerator().getNoiseBiome(position.getX(), position.getY(), position.getZ()).equals(Biome.BiomeCategory.NETHER)) {
               NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(position.getX(), position.getZ(), context.heightAccessor());
               int i = 0;
               while (!blockReader.getBlock(i).isAir()) {
                  i++;
               }
               position = new BlockPos(position.getX(), i, position.getZ());
               return JigsawPlacement.addPieces(newContext, PoolElementStructurePiece::new, position, false, false);
            } else {
               return JigsawPlacement.addPieces(newContext, PoolElementStructurePiece::new, position, false, true);
            }
         }
      });
      this.name = nameIn;
      this.structureConfig = config;
      this.setRegistryName(this.getFeatureName());
   }

   @Override
   public GenerationStep.Decoration step() {
      return GenerationStep.Decoration.SURFACE_STRUCTURES;
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

   public StructureData getConfig() {
      return this.structureConfig;
   }

   public static boolean checkLocation(PieceGeneratorSupplier.Context<JigsawConfiguration> context, StructureData structureConfig) {
      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(context.seed()));
      worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
      List<StructureFeature<?>> structures = new ArrayList<>();

      for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
         structures.add(holder.getStructure());
      }
      structures.add(StructureFeature.VILLAGE);
      if (!ShrinesStructure.checkForOtherStructures(structureConfig, context.chunkGenerator(), context.seed(), context.chunkPos(), structures)) {
         return false;
      }
      if (context.chunkGenerator().getFirstFreeHeight(context.chunkPos().x, context.chunkPos().z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor()) < 60) {
         return false;
      }
      return worldgenrandom.nextDouble() < structureConfig.getSpawn_chance();
   }

   public static boolean checkForOtherStructures(StructureData structureConfig, ChunkGenerator generator, long seed,
                                                 ChunkPos chunkPos, List<StructureFeature<?>> structures) {
      for (StructureFeature<?> structure1 : structures) {
         StructureFeatureConfiguration separationSettings = generator.getSettings().getConfig(structure1);

         if (separationSettings == null || (structure1 instanceof ShrinesStructure && ((ShrinesStructure) structure1).structureConfig.getKey().equals(structureConfig.getKey()))) {
            continue;
         }

         int distance = Config.SETTINGS.STRUCTURE_MIN_DISTANCE.get();
         for (int x = chunkPos.x - distance; x <= chunkPos.x + distance; x++) {
            for (int z = chunkPos.z - distance; z <= chunkPos.z + distance; z++) {
               ChunkPos structurePos = structure1.getPotentialFeatureChunk(separationSettings, seed, x, z);

               if (x == structurePos.x && z == structurePos.z) {
                  return false;
               }
            }
         }
      }

      return true;
   }
}