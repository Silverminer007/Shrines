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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.jetbrains.annotations.NotNull;

public class LocateInBiomeCommand {
   private static final DynamicCommandExceptionType ERROR_FAILED = new DynamicCommandExceptionType((args) -> new TranslatableComponent("commands.locate.failed", args));
   private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType((args) -> new TranslatableComponent("commands.locate.invalid", args));

   public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
      commandDispatcher.register(Commands.literal("locateshrines").requires((sourceStack) ->
            sourceStack.hasPermission(2)
      ).then(Commands.literal("inbiome")
            .then(Commands.argument("structure", ResourceOrTagLocationArgument.resourceOrTag(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY))
                  .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY)).executes((commandContext) ->
                        locateInBiome(commandContext.getSource(), ResourceOrTagLocationArgument.getStructureFeature(commandContext, "structure"), ResourceOrTagLocationArgument.getBiome(commandContext, "biome")))))
      ).then(Commands.literal("new")
            .then(Commands.argument("structure", ResourceOrTagLocationArgument.resourceOrTag(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)).executes(commandContext ->
                  locateNew(commandContext.getSource(), ResourceOrTagLocationArgument.getStructureFeature(commandContext, "structure"))))));
   }

   private static int locateNew(@NotNull CommandSourceStack commandSourceStack, ResourceOrTagLocationArgument.@NotNull Result<ConfiguredStructureFeature<?, ?>> structureFeatureResult) throws CommandSyntaxException {
      Registry<ConfiguredStructureFeature<?, ?>> registry = commandSourceStack.getLevel().registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
      HolderSet<ConfiguredStructureFeature<?, ?>> holderset = structureFeatureResult.unwrap().map((resourceKey) ->
                  registry.getHolder(resourceKey).map(HolderSet::direct), registry::getTag)
            .orElseThrow(() -> ERROR_INVALID.create(structureFeatureResult.asPrintable()));
      BlockPos blockpos = new BlockPos(commandSourceStack.getPosition());
      ServerLevel serverlevel = commandSourceStack.getLevel();
      Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = serverlevel.getChunkSource().getGenerator().findNearestMapFeature(serverlevel, holderset, blockpos, 100, true);
      if (pair == null) {
         throw ERROR_FAILED.create(structureFeatureResult.asPrintable());
      } else {
         return showLocateResult(commandSourceStack, structureFeatureResult, blockpos, pair, "commands.locate.success");
      }
   }

   private static int locateInBiome(@NotNull CommandSourceStack commandSourceStack, ResourceOrTagLocationArgument.@NotNull Result<ConfiguredStructureFeature<?, ?>> structureFeatureResult, ResourceOrTagLocationArgument.Result<Biome> biomeResult) throws CommandSyntaxException {
      Registry<ConfiguredStructureFeature<?, ?>> registry = commandSourceStack.getLevel().registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
      HolderSet<ConfiguredStructureFeature<?, ?>> holderset = structureFeatureResult.unwrap().map((resourceKey) ->
                  registry.getHolder(resourceKey).map(HolderSet::direct), registry::getTag)
            .orElseThrow(() -> ERROR_INVALID.create(structureFeatureResult.asPrintable()));
      BlockPos blockpos = new BlockPos(commandSourceStack.getPosition());
      ServerLevel serverlevel = commandSourceStack.getLevel();
      Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = ((LocateInBiomeChunkGenerator)serverlevel.getChunkSource().getGenerator()).findNearestMapFeature(serverlevel, holderset, blockpos, 100, false, biomeResult);
      if (pair == null) {
         throw ERROR_FAILED.create(structureFeatureResult.asPrintable());
      } else {
         return showLocateResult(commandSourceStack, structureFeatureResult, blockpos, pair, "commands.locate.success");
      }
   }

   private static int showLocateResult(@NotNull CommandSourceStack commandSourceStack, ResourceOrTagLocationArgument.@NotNull Result<?> structureResult, @NotNull BlockPos blockPos, @NotNull Pair<BlockPos, ? extends Holder<?>> foundStructure, String message) {
      BlockPos blockpos = foundStructure.getFirst();
      String s = structureResult.unwrap().map((p_207538_) -> p_207538_.location().toString(), (p_207511_) -> "#" + p_207511_.location() + " (" + foundStructure.getSecond().unwrapKey().map((p_207536_) -> p_207536_.location().toString()).orElse("[unregistered]") + ")");
      int i = Mth.floor(dist(blockPos.getX(), blockPos.getZ(), blockpos.getX(), blockpos.getZ()));
      Component component = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("chat.coordinates", blockpos.getX(), "~", blockpos.getZ())).withStyle((p_207527_) ->
            p_207527_.withColor(ChatFormatting.GREEN).withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + blockpos.getX() + " ~ " + blockpos.getZ()))
                  .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip"))));
      commandSourceStack.sendSuccess(new TranslatableComponent(message, s, component, i), false);
      return i;
   }

   private static float dist(int pX1, int pZ1, int pX2, int pZ2) {
      int i = pX2 - pX1;
      int j = pZ2 - pZ1;
      return Mth.sqrt((float) (i * i + j * j));
   }
}