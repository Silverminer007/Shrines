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
import com.silverminer.shrines.registry.Utils;
import com.silverminer.shrines.structures.ShrinesJigsawPlacement;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class VariationCommand {
   private static final DynamicCommandExceptionType ERROR_INVALID_CONFIG = new DynamicCommandExceptionType((o) -> new TranslatableComponent("commands.variate.invalid.config", o));
   private static final DynamicCommandExceptionType ERROR_INVALID_TEMPLATE = new DynamicCommandExceptionType((o) -> new TranslatableComponent("commands.variate.invalid.template", o));

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
                                                      getTemnplatePool(context, "startPool"),
                                                      IntegerArgumentType.getInteger(context, "maxDepth"),
                                                      BoolArgumentType.getBool(context, "keepJigsaws"),
                                                      BlockPosArgument.getLoadedBlockPos(context, "structurePos"),
                                                      getVariationConfig(context, "config"))))))))));
   }

   private static int variate(CommandSourceStack source, RandomVariationConfig config, BlockPos from, BlockPos to) {
      BoundingBox box = BoundingBox.fromCorners(from, to);
      config.process(source.getLevel(), createRandom(), box, box, box::isInside);
      return 0;
   }

   private static int loadAndVariate(CommandSourceStack source, ResourceLocation template, BlockPos structurePos, RandomVariationConfig config) {
      StructureManager structuremanager = source.getLevel().getStructureManager();

      Optional<StructureTemplate> optional;
      try {
         optional = structuremanager.get(template);
      } catch (ResourceLocationException resourcelocationexception) {
         return -1;
      }

      if (optional.isEmpty()) {
         return -1;
      }
      Random random = createRandom();
      StructureTemplate structureTemplate = optional.get();
      StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
      structureTemplate.placeInWorld(source.getLevel(), structurePos, structurePos, structureplacesettings, random, 2);
      BoundingBox box = structureTemplate.getBoundingBox(structureplacesettings, structurePos);
      config.process(source.getLevel(), random, box, box, box::isInside);
      return 0;
   }

   private static Random createRandom() {
      return new Random(Util.getMillis());
   }

   private static int loadJigsawAndVariate(ServerLevel serverLevel, StructureTemplatePool structureTemplatePool, int maxDepth, boolean keepJigsaws, BlockPos structurePos, RandomVariationConfig config) {
      ChunkGenerator chunkgenerator = serverLevel.getChunkSource().getGenerator();
      StructureManager structuremanager = serverLevel.getStructureManager();
      StructureFeatureManager structurefeaturemanager = serverLevel.structureFeatureManager();
      Random random = serverLevel.getRandom();
      List<PoolElementStructurePiece> list = Lists.newArrayList();
      StructurePoolElement structurepoolelement = structureTemplatePool.getRandomTemplate(random);
      PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(structuremanager, structurepoolelement, structurePos, 1, Rotation.NONE, structurepoolelement.getBoundingBox(structuremanager, structurePos, Rotation.NONE));
      list.add(poolelementstructurepiece);// TODO Take care about the bounds of the first structure template
      ShrinesJigsawPlacement.addPieces(serverLevel.registryAccess(), poolelementstructurepiece, maxDepth, PoolElementStructurePiece::new, chunkgenerator, structuremanager, list, random, serverLevel);

      for (PoolElementStructurePiece poolelementstructurepiece1 : list) {
         poolelementstructurepiece1.place(serverLevel, structurefeaturemanager, chunkgenerator, random, BoundingBox.infinite(), structurePos, keepJigsaws);
      }

      if (list.isEmpty()) {
         return 0;
      }
      PiecesContainer container = new PiecesContainer(list.stream().map(o -> (StructurePiece) o).toList());
      BoundingBox box = container.calculateBoundingBox();
      config.process(serverLevel, random, box, box, container::isInsidePiece);
      return 0;
   }

   @SuppressWarnings("unchecked")
   private static RandomVariationConfig getVariationConfig(CommandContext<CommandSourceStack> commandContext, String literal) throws CommandSyntaxException {
      ResourceKey<RandomVariationConfig> resourcekey = getRegistryType(commandContext, literal, (ResourceKey<Registry<RandomVariationConfig>>) RandomVariationConfig.REGISTRY, ERROR_INVALID_CONFIG);
      return getRegistry(RandomVariationConfig.REGISTRY).getOptional(resourcekey).orElseThrow(() ->
            ERROR_INVALID_CONFIG.create(resourcekey.location())
      );
   }

   private static StructureTemplatePool getTemnplatePool(CommandContext<CommandSourceStack> commandContext, String literal) throws CommandSyntaxException {
      ResourceKey<StructureTemplatePool> resourcekey = getRegistryType(commandContext, literal, Registry.TEMPLATE_POOL_REGISTRY, ERROR_INVALID_TEMPLATE);
      return getRegistry(Registry.TEMPLATE_POOL_REGISTRY).getOptional(resourcekey).orElseThrow(() ->
            ERROR_INVALID_TEMPLATE.create(resourcekey.location())
      );
   }

   private static <T> ResourceKey<T> getRegistryType(CommandContext<CommandSourceStack> commandContext, String literal, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exceptionType) throws CommandSyntaxException {
      ResourceKey<?> resourcekey = commandContext.getArgument(literal, ResourceKey.class);
      Optional<ResourceKey<T>> optional = resourcekey.cast(registryKey);
      return optional.orElseThrow(() ->
            exceptionType.create(resourcekey)
      );
   }

   private static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
      return Utils.getRegistry(key.location());
   }
}