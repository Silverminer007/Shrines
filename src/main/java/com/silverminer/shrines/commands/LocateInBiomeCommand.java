/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.*;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import net.minecraft.commands.arguments.ResourceOrTagLocationArgument.Result;

public class LocateInBiomeCommand {
   private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType((args) -> {
      return Component.translatable("commands.locate.structure.invalid", args);
   });
   private static final DynamicCommandExceptionType ERROR_BIOME_INVALID = new DynamicCommandExceptionType((args) -> {
      return Component.translatable("commands.locate.biome.invalid", args);
   });
   private static final DynamicCommandExceptionType ERROR_STRUCTURE_NOT_FOUND = new DynamicCommandExceptionType((p_201831_) -> {
      return Component.translatable("commands.locate.structure.not_found", p_201831_);
   });

   public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
      commandDispatcher.register(Commands.literal("locateshrines").requires((sourceStack) ->
            sourceStack.hasPermission(2)
      ).then(Commands.literal("inbiome")
            .then(Commands.argument("structure", ResourceOrTagLocationArgument.resourceOrTag(Registry.STRUCTURE_REGISTRY))
                  .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY)).executes((commandContext) ->
                        locateInBiome(commandContext.getSource(),
                              ResourceOrTagLocationArgument.getRegistryType(commandContext, "structure", Registry.STRUCTURE_REGISTRY, ERROR_STRUCTURE_INVALID),
                              ResourceOrTagLocationArgument.getRegistryType(commandContext, "biome", Registry.BIOME_REGISTRY, ERROR_BIOME_INVALID)))))
      ).then(Commands.literal("new")
            .then(Commands.argument("structure", ResourceOrTagLocationArgument.resourceOrTag(Registry.STRUCTURE_REGISTRY)).executes(commandContext ->
                  locateNew(commandContext.getSource(), ResourceOrTagLocationArgument.getRegistryType(commandContext, "structure", Registry.STRUCTURE_REGISTRY, ERROR_STRUCTURE_INVALID))))));
   }

   private static int locateNew(@NotNull CommandSourceStack commandSourceStack, ResourceOrTagLocationArgument.@NotNull Result<Structure> structureResult) throws CommandSyntaxException {
      Registry<Structure> registry = commandSourceStack.getLevel().registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
      HolderSet<Structure> holderset = structureResult.unwrap().map((resourceKey) ->
                  registry.getHolder(resourceKey).map(HolderSet::direct), registry::getTag)
            .orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(structureResult.asPrintable()));
      BlockPos blockpos = new BlockPos(commandSourceStack.getPosition());
      ServerLevel serverlevel = commandSourceStack.getLevel();
      Pair<BlockPos, Holder<Structure>> pair = serverlevel.getChunkSource().getGenerator().findNearestMapStructure(serverlevel, holderset, blockpos, 100, true);
      if (pair == null) {
         throw ERROR_STRUCTURE_NOT_FOUND.create(structureResult.asPrintable());
      } else {
         return LocateCommand.showLocateResult(commandSourceStack, structureResult, blockpos, pair, "commands.locate.structure.success", false);
      }
   }

   private static int locateInBiome(@NotNull CommandSourceStack commandSourceStack, ResourceOrTagLocationArgument.@NotNull Result<Structure> structureFeatureResult, ResourceOrTagLocationArgument.Result<Biome> biomeResult) throws CommandSyntaxException {
      Registry<Structure> registry = commandSourceStack.getLevel().registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
      HolderSet<Structure> holderset = structureFeatureResult.unwrap().map((resourceKey) ->
                  registry.getHolder(resourceKey).map(HolderSet::direct), registry::getTag)
            .orElseThrow(() -> ERROR_STRUCTURE_NOT_FOUND.create(structureFeatureResult.asPrintable()));
      BlockPos blockpos = new BlockPos(commandSourceStack.getPosition());
      ServerLevel serverlevel = commandSourceStack.getLevel();
      Pair<BlockPos, Holder<Structure>> pair = ((LocateInBiomeChunkGenerator)serverlevel.getChunkSource().getGenerator()).findNearestMapStructure(serverlevel, holderset, blockpos, 100, false, biomeResult);
      if (pair == null) {
         throw ERROR_STRUCTURE_NOT_FOUND.create(structureFeatureResult.asPrintable());
      } else {
         return LocateCommand.showLocateResult(commandSourceStack, structureFeatureResult, blockpos, pair, "commands.locate.structure.success", false);
      }
   }
}