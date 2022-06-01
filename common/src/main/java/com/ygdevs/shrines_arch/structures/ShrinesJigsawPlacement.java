/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public class ShrinesJigsawPlacement {
   static final Logger LOGGER = LogUtils.getLogger();

   public static <T extends JigsawConfiguration> Optional<PieceGenerator<T>> addPieces(PieceGeneratorSupplier.Context<T> p_210285_, PieceFactory p_210286_, BlockPos p_210287_, boolean p_210288_, boolean p_210289_) {
      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
      worldgenrandom.setLargeFeatureSeed(p_210285_.seed(), p_210285_.chunkPos().x, p_210285_.chunkPos().z);
      RegistryAccess registryaccess = p_210285_.registryAccess();
      JigsawConfiguration jigsawconfiguration = p_210285_.config();
      ChunkGenerator chunkgenerator = p_210285_.chunkGenerator();
      StructureManager structuremanager = p_210285_.structureManager();
      LevelHeightAccessor levelheightaccessor = p_210285_.heightAccessor();
      Predicate<Holder<Biome>> predicate = p_210285_.validBiome();
      StructureFeature.bootstrap();
      Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
      Rotation rotation = Rotation.getRandom(worldgenrandom);
      StructureTemplatePool structuretemplatepool = jigsawconfiguration.startPool().value();
      StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
      if (structurepoolelement == EmptyPoolElement.INSTANCE) {
         return Optional.empty();
      } else {
         PoolElementStructurePiece poolelementstructurepiece = p_210286_.create(structuremanager, structurepoolelement, p_210287_, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuremanager, p_210287_, rotation));
         BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
         int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
         int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
         int k;
         if (p_210289_) {
            k = p_210287_.getY() + chunkgenerator.getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, levelheightaccessor);
         } else {
            k = p_210287_.getY();
         }

         if (!predicate.test(chunkgenerator.getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j)))) {
            return Optional.empty();
         } else {
            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            return Optional.of((p_210282_, p_210283_) -> {
               List<PoolElementStructurePiece> list = Lists.newArrayList();
               list.add(poolelementstructurepiece);
               if (jigsawconfiguration.maxDepth() > 0) {
                  int i1 = 80;
                  AABB aabb = new AABB((double)(i - 80), (double)(k - 80), (double)(j - 80), (double)(i + 80 + 1), (double)(k + 80 + 1), (double)(j + 80 + 1));
                  Placer jigsawplacement$placer = new Placer(registry, jigsawconfiguration.maxDepth(), p_210286_, chunkgenerator, structuremanager, list, worldgenrandom);
                  jigsawplacement$placer.placing.addLast(new PieceState(poolelementstructurepiece, new MutableObject<>(Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST)), 0));

                  while(!jigsawplacement$placer.placing.isEmpty()) {
                     PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
                     jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, p_210288_, levelheightaccessor);
                  }

                  list.forEach(p_210282_::addPiece);
               }
            });
         }
      }
   }

   public static void addPieces(RegistryAccess pRegistryAccess, PoolElementStructurePiece pPiece, int pMaxDepth, PieceFactory pFactory, ChunkGenerator pChunkGenerator, StructureManager pStructureManager, List<? super PoolElementStructurePiece> pPieces, Random pRandom, LevelHeightAccessor pLevel) {
      Registry<StructureTemplatePool> registry = pRegistryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
      Placer jigsawplacement$placer = new Placer(registry, pMaxDepth, pFactory, pChunkGenerator, pStructureManager, pPieces, pRandom);
      jigsawplacement$placer.placing.addLast(new PieceState(pPiece, new MutableObject<>(Shapes.INFINITY), 0));

      while(!jigsawplacement$placer.placing.isEmpty()) {
         PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
         jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, false, pLevel);
      }

   }

   public interface PieceFactory {
      PoolElementStructurePiece create(StructureManager p_210301_, StructurePoolElement p_210302_, BlockPos p_210303_, int p_210304_, Rotation p_210305_, BoundingBox p_210306_);
   }

   static final class PieceState {
      final PoolElementStructurePiece piece;
      final MutableObject<VoxelShape> free;
      final int depth;

      PieceState(PoolElementStructurePiece pPiece, MutableObject<VoxelShape> pFree, int pDepth) {
         this.piece = pPiece;
         this.free = pFree;
         this.depth = pDepth;
      }
   }

   static final class Placer {
      private final Registry<StructureTemplatePool> pools;
      private final int maxDepth;
      private final PieceFactory factory;
      private final ChunkGenerator chunkGenerator;
      private final StructureManager structureManager;
      private final List<? super PoolElementStructurePiece> pieces;
      private final Random random;
      final Deque<PieceState> placing = Queues.newArrayDeque();

      Placer(Registry<StructureTemplatePool> pPools, int pMaxDepth, PieceFactory pFactory, ChunkGenerator pChunkGenerator, StructureManager pStructureManager, List<? super PoolElementStructurePiece> pPieces, Random pRandom) {
         this.pools = pPools;
         this.maxDepth = pMaxDepth;
         this.factory = pFactory;
         this.chunkGenerator = pChunkGenerator;
         this.structureManager = pStructureManager;
         this.pieces = pPieces;
         this.random = pRandom;
      }

      void tryPlacingChildren(PoolElementStructurePiece p_210334_, MutableObject<VoxelShape> p_210335_, int p_210336_, boolean p_210337_, LevelHeightAccessor p_210338_) {
         StructurePoolElement structurepoolelement = p_210334_.getElement();
         BlockPos blockpos = p_210334_.getPosition();
         Rotation rotation = p_210334_.getRotation();
         StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
         boolean flag = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
         MutableObject<VoxelShape> mutableobject = new MutableObject<>();
         BoundingBox boundingbox = p_210334_.getBoundingBox();
         int i = boundingbox.minY();

         label139:
         for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : structurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
            Direction direction = JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state);
            BlockPos blockpos1 = structuretemplate$structureblockinfo.pos;
            BlockPos blockpos2 = blockpos1.relative(direction);
            int j = blockpos1.getY() - i;
            int k = -1;
            ResourceLocation resourcelocation = new ResourceLocation(structuretemplate$structureblockinfo.nbt.getString("pool"));
            Optional<StructureTemplatePool> optional = this.pools.getOptional(resourcelocation);
            if (optional.isPresent() && (optional.get().size() != 0 || Objects.equals(resourcelocation, Pools.EMPTY.location()))) {
               ResourceLocation resourcelocation1 = optional.get().getFallback();
               Optional<StructureTemplatePool> optional1 = this.pools.getOptional(resourcelocation1);
               if (optional1.isPresent() && (optional1.get().size() != 0 || Objects.equals(resourcelocation1, Pools.EMPTY.location()))) {
                  boolean flag1 = boundingbox.isInside(blockpos2);
                  MutableObject<VoxelShape> mutableobject1;
                  if (flag1) {
                     mutableobject1 = mutableobject;
                     if (mutableobject.getValue() == null) {
                        mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
                     }
                  } else {
                     mutableobject1 = p_210335_;
                  }

                  List<StructurePoolElement> list = Lists.newArrayList();
                  if (p_210336_ != this.maxDepth) {
                     list.addAll(optional.get().getShuffledTemplates(this.random));
                  }

                  list.addAll(optional1.get().getShuffledTemplates(this.random));

                  for(StructurePoolElement structurepoolelement1 : list) {
                     if (structurepoolelement1 == EmptyPoolElement.INSTANCE) {
                        break;
                     }

                     for(Rotation rotation1 : Rotation.getShuffled(this.random)) {
                        List<StructureTemplate.StructureBlockInfo> list1 = structurepoolelement1.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation1, this.random);
                        BoundingBox boundingbox1 = structurepoolelement1.getBoundingBox(this.structureManager, BlockPos.ZERO, rotation1);
                        int l;
                        if (p_210337_ && boundingbox1.getYSpan() <= 16) {
                           l = list1.stream().mapToInt((p_210332_) -> {
                              if (!boundingbox1.isInside(p_210332_.pos.relative(JigsawBlock.getFrontFacing(p_210332_.state)))) {
                                 return 0;
                              } else {
                                 ResourceLocation resourcelocation2 = new ResourceLocation(p_210332_.nbt.getString("pool"));
                                 Optional<StructureTemplatePool> optional2 = this.pools.getOptional(resourcelocation2);
                                 Optional<StructureTemplatePool> optional3 = optional2.flatMap((p_210344_) -> {
                                    return this.pools.getOptional(p_210344_.getFallback());
                                 });
                                 int j3 = optional2.map((p_210342_) -> {
                                    return p_210342_.getMaxSize(this.structureManager);
                                 }).orElse(0);
                                 int k3 = optional3.map((p_210340_) -> {
                                    return p_210340_.getMaxSize(this.structureManager);
                                 }).orElse(0);
                                 return Math.max(j3, k3);
                              }
                           }).max().orElse(0);
                        } else {
                           l = 0;
                        }

                        for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 : list1) {
                           if (JigsawBlock.canAttach(structuretemplate$structureblockinfo, structuretemplate$structureblockinfo1)) {
                              BlockPos blockpos3 = structuretemplate$structureblockinfo1.pos;
                              BlockPos blockpos4 = blockpos2.subtract(blockpos3);
                              BoundingBox boundingbox2 = structurepoolelement1.getBoundingBox(this.structureManager, blockpos4, rotation1);
                              int i1 = boundingbox2.minY();
                              StructureTemplatePool.Projection structuretemplatepool$projection1 = structurepoolelement1.getProjection();
                              boolean flag2 = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                              int j1 = blockpos3.getY();
                              int k1 = j - j1 + JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state).getStepY();
                              int l1;
                              if (flag && flag2) {
                                 l1 = i + k1;
                              } else {
                                 if (k == -1) {
                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, p_210338_);
                                 }

                                 l1 = k - j1;
                              }

                              int i2 = l1 - i1;
                              BoundingBox boundingbox3 = boundingbox2.moved(0, i2, 0);
                              BlockPos blockpos5 = blockpos4.offset(0, i2, 0);
                              if (l > 0) {
                                 int j2 = Math.max(l + 1, boundingbox3.maxY() - boundingbox3.minY());
                                 boundingbox3.encapsulate(new BlockPos(boundingbox3.minX(), boundingbox3.minY() + j2, boundingbox3.minZ()));
                              }

                              if (!Shapes.joinIsNotEmpty(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                 mutableobject1.setValue(Shapes.joinUnoptimized(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3)), BooleanOp.ONLY_FIRST));
                                 int i3 = p_210334_.getGroundLevelDelta();
                                 int k2;
                                 if (flag2) {
                                    k2 = i3 - k1;
                                 } else {
                                    k2 = structurepoolelement1.getGroundLevelDelta();
                                 }

                                 PoolElementStructurePiece poolelementstructurepiece = this.factory.create(this.structureManager, structurepoolelement1, blockpos5, k2, rotation1, boundingbox3);
                                 int l2;
                                 if (flag) {
                                    l2 = i + j;
                                 } else if (flag2) {
                                    l2 = l1 + j1;
                                 } else {
                                    if (k == -1) {
                                       k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, p_210338_);
                                    }

                                    l2 = k + k1 / 2;
                                 }

                                 p_210334_.addJunction(new JigsawJunction(blockpos2.getX(), l2 - j + i3, blockpos2.getZ(), k1, structuretemplatepool$projection1));
                                 poolelementstructurepiece.addJunction(new JigsawJunction(blockpos1.getX(), l2 - j1 + k2, blockpos1.getZ(), -k1, structuretemplatepool$projection));
                                 this.pieces.add(poolelementstructurepiece);
                                 if (p_210336_ + 1 <= this.maxDepth) {
                                    this.placing.addLast(new PieceState(poolelementstructurepiece, mutableobject1, p_210336_ + 1));
                                 }
                                 continue label139;
                              }
                           }
                        }
                     }
                  }
               } else {
                  LOGGER.warn("Empty or non-existent fallback pool: {}", (Object)resourcelocation1);
               }
            } else {
               LOGGER.warn("Empty or non-existent pool: {}", (Object)resourcelocation);
            }
         }

      }
   }
}