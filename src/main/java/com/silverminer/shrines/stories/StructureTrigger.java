/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.stories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.capabilities.CapabilityManager;
import com.silverminer.shrines.registries.TriggerTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class StructureTrigger implements Trigger {
   public static final Codec<StructureTrigger> CODEC = RecordCodecBuilder.create(structureTriggerInstance ->
         structureTriggerInstance.group(Structure.CODEC.fieldOf("structure").forGetter(StructureTrigger::getStructure))
               .apply(structureTriggerInstance, StructureTrigger::new));
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
      LazyOptional<CapabilityManager.IStoryCapability> storyCapabilityLazyOptional = player.getCapability(CapabilityManager.STORY);
      if(storyCapabilityLazyOptional.filter(iStoryCapability -> iStoryCapability.getMarkedStructures().stream().noneMatch(boundingBox -> boundingBox.isInside(pos))).isPresent()){
         storyCapabilityLazyOptional.ifPresent(iStoryCapability -> iStoryCapability.markStructure(structureBounds));
         return true;
      }
      return false;
   }

   protected Holder<Structure> getStructure() {
      return structure;
   }
}