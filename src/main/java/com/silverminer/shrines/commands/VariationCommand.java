/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.silverminer.shrines.random_variation.RandomVariationConfig;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VariationCommand {
   private static final DynamicCommandExceptionType ERROR_INVALID_CONFIG = new DynamicCommandExceptionType((o) -> Component.translatable("commands.variate.invalid.config", o));
   private static final DynamicCommandExceptionType ERROR_INVALID_TEMPLATE = new DynamicCommandExceptionType((o) -> Component.translatable("commands.variate.invalid.template", o));

   public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
      pDispatcher.register(Commands.literal("variate").requires((commandSourceStack) ->
                  commandSourceStack.hasPermission(2))
            .then(Commands.literal("existing")
                  .then(Commands.argument("config", ResourceKeyArgument.key(RandomVariationConfig.REGISTRY))
                        .then(Commands.argument("from", BlockPosArgument.blockPos())
                              .then(Commands.argument("to", BlockPosArgument.blockPos()).executes((commandContext) ->
                                    variate(commandContext.getSource(), getVariationConfig(commandContext, "config"),
                                          BlockPosArgument.getLoadedBlockPos(commandContext, "from"), BlockPosArgument.getLoadedBlockPos(commandContext, "to")))))))
            .then(Commands.literal("template").then(Commands.argument("template", ResourceLocationArgument.id())
                  .then(Commands.argument("structurePos", BlockPosArgument.blockPos())
                        .then(Commands.argument("config", ResourceKeyArgument.key(RandomVariationConfig.REGISTRY)).executes((context) ->
                              loadAndVariate(context.getSource(), ResourceLocationArgument.getId(context, "template"),
                                    BlockPosArgument.getLoadedBlockPos(context, "structurePos"),
                                    getVariationConfig(context, "config")))))))
            .then(Commands.literal("jigsaw")
                  .then(Commands.argument("startPool", ResourceKeyArgument.key(Registry.TEMPLATE_POOL_REGISTRY))
                        .then(Commands.argument("maxDepth", IntegerArgumentType.integer(1, 20))
                              .then(Commands.argument("keepJigsaws", BoolArgumentType.bool())
                                    .then(Commands.argument("structurePos", BlockPosArgument.blockPos())
                                          .then(Commands.argument("config", ResourceKeyArgument.key(RandomVariationConfig.REGISTRY)).executes((context) ->
                                                loadJigsawAndVariate(context.getSource().getLevel(),
                                                      ResourceKeyArgument.getStructureTemplatePool(context, "startPool"),
                                                      IntegerArgumentType.getInteger(context, "maxDepth"),
                                                      BoolArgumentType.getBool(context, "keepJigsaws"),
                                                      BlockPosArgument.getLoadedBlockPos(context, "structurePos"),
                                                      getVariationConfig(context, "config"))))))))));
   }

   private static int variate(CommandSourceStack source, RandomVariationConfig config, BlockPos from, BlockPos to) {
      BoundingBox box = BoundingBox.fromCorners(from, to);
      config.process(source.getLevel(), source.getLevel().getRandom(), box, box, box::isInside);
      return 0;
   }

   private static int loadAndVariate(CommandSourceStack source, ResourceLocation template, BlockPos structurePos, RandomVariationConfig config) {
      StructureTemplateManager structuremanager = source.getLevel().getStructureManager();

      Optional<StructureTemplate> optional;
      try {
         optional = structuremanager.get(template);
      } catch (ResourceLocationException resourcelocationexception) {
         return -1;
      }

      if (optional.isEmpty()) {
         return -1;
      }
      RandomSource random = source.getLevel().getRandom();
      StructureTemplate structureTemplate = optional.get();
      StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
      structureTemplate.placeInWorld(source.getLevel(), structurePos, structurePos, structureplacesettings, random, 2);
      BoundingBox box = structureTemplate.getBoundingBox(structureplacesettings, structurePos);
      config.process(source.getLevel(), random, box, box, box::isInside);
      return 0;
   }

   private static int loadJigsawAndVariate(ServerLevel serverLevel, Holder<StructureTemplatePool> structureTemplatePool, int maxDepth, boolean keepJigsaws, BlockPos structurePos, RandomVariationConfig config) {
      ChunkGenerator chunkgenerator = serverLevel.getChunkSource().getGenerator();
      StructureTemplateManager structureTemplateManager = serverLevel.getStructureManager();
      StructureManager structureManager = serverLevel.structureManager();
      RandomSource random = serverLevel.getRandom();
      List<PoolElementStructurePiece> list = Lists.newArrayList();
      StructurePoolElement structurepoolelement = structureTemplatePool.value().getRandomTemplate(random);
      PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(structureTemplateManager, structurepoolelement, structurePos, 1, Rotation.NONE, structurepoolelement.getBoundingBox(structureTemplateManager, structurePos, Rotation.NONE));
      list.add(poolelementstructurepiece);// TODO Take care about the bounds of the first structure template
      JigsawPlacement.generateJigsaw(serverLevel, structureTemplatePool, new ResourceLocation("empty"), maxDepth, structurePos, keepJigsaws);// TODO Jigsaw start command param

      for (PoolElementStructurePiece poolelementstructurepiece1 : list) {
         poolelementstructurepiece1.place(serverLevel, structureManager, chunkgenerator, random, BoundingBox.infinite(), structurePos, keepJigsaws);
      }

      PiecesContainer container = new PiecesContainer(list.stream().map(o -> (StructurePiece) o).toList());
      BoundingBox box = container.calculateBoundingBox();
      config.process(serverLevel, random, box, box, container::isInsidePiece);
      return 0;
   }

   @SuppressWarnings("unchecked")
   private static RandomVariationConfig getVariationConfig(CommandContext<CommandSourceStack> commandContext, String literal) throws CommandSyntaxException {
      ResourceKey<RandomVariationConfig> resourcekey = getRegistryType(commandContext, literal, (ResourceKey<Registry<RandomVariationConfig>>) RandomVariationConfig.REGISTRY, ERROR_INVALID_CONFIG);
      return commandContext.getSource().getLevel().getServer().registryAccess().registryOrThrow(RandomVariationConfig.REGISTRY).getOptional(resourcekey).orElseThrow(() ->
            ERROR_INVALID_CONFIG.create(resourcekey.location())
      );
   }

   private static <T> ResourceKey<T> getRegistryType(CommandContext<CommandSourceStack> commandContext, String literal, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
      ResourceKey<?> resourcekey = commandContext.getArgument(literal, ResourceKey.class);
      Optional<ResourceKey<T>> optional = resourcekey.cast(registryKey);
      return optional.orElseThrow(() ->
            exceptionType.create(resourcekey)
      );
   }
}