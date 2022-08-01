/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.stories;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.capabilities.CapabilityManager;
import com.silverminer.shrines.registries.TriggerTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class StructureTrigger implements Trigger {
   public static final Codec<StructureTrigger> CODEC = Structure.CODEC.comapFlatMap(to -> DataResult.success(new StructureTrigger(to)), StructureTrigger::getStructure);
   private final Holder<Structure> structure;

   public StructureTrigger(Holder<Structure> structure) {
      this.structure = structure;
   }

   @Override
   public TriggerType getType() {
      return TriggerTypeRegistry.STRUCTURE.get();
   }

   @Override
   public boolean matches(@NotNull Level level, @NotNull Player player) {
      BlockPos pos = player.blockPosition();
      if (!level.getChunkAt(pos).hasAnyStructureReferences()) {
         return false;
      }
      StructureStart structureStart = level.getChunkAt(pos).getStartForStructure(this.structure.get());
      if (structureStart == null) {
         return false;
      }
      BoundingBox structureBounds = structureStart.getBoundingBox();
      if (!structureBounds.isInside(pos)) {
         return false;
      }
      ResourceLocation structureKey = Shrines.location("structures");
      LazyOptional<CapabilityManager.IStoryCapability> storyCapabilityLazyOptional = player.getCapability(CapabilityManager.STORY);
      if(storyCapabilityLazyOptional.filter(iStoryCapability -> iStoryCapability.getSaves(structureKey).stream().noneMatch(boundingBox -> boxFromString(boundingBox).map(box -> box.isInside(pos)).orElse(true))).isPresent()){
         storyCapabilityLazyOptional.ifPresent(iStoryCapability -> boxToString(structureBounds).ifPresent(s ->iStoryCapability.addSavedData(structureKey, s)));
         return true;
      }
      return false;
   }

   private static Optional<BoundingBox> boxFromString(String input) {
      return BoundingBox.CODEC.decode(JsonOps.COMPRESSED, JsonParser.parseString(input)).result().map(Pair::getFirst);
   }

   private static Optional<String> boxToString(BoundingBox input) {
      return BoundingBox.CODEC.encodeStart(JsonOps.COMPRESSED, input).result().map(Objects::toString);
   }

   protected Holder<Structure> getStructure() {
      return structure;
   }
}