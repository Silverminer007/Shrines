/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.saves;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.custom.helper.ResourceData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

/**
 * @author Silverminer
 */
public class BoundSaveData extends WorldSavedData {
   protected static final Logger LOG = LogManager.getLogger(BoundSaveData.class);
   private static final String DATA_NAME = "shrines-bounds";

   public static BoundSaveData get(ServerWorld world) {
      if (world == null)
         return null;
      LOG.debug("BoundSaveData read");
      DimensionSavedDataManager storage = world.getDataStorage();

      return storage.computeIfAbsent(BoundSaveData::new, DATA_NAME);
   }

   /**
    *
    */
   public BoundSaveData() {
      super(DATA_NAME);
   }

   @Override
   public void load(CompoundNBT nbt) {
      int count = nbt.getInt("structures");
      for (int i = 0; i < count; i++) {
         CompoundNBT tag = nbt.getCompound(String.valueOf(i));
         CustomStructureData csd = Utils.getData(tag.getString("structure"), true);
         if (csd == null) {
            LOG.info("Failed to load structure {}", tag.getString("structure"));
            continue;
         }
         csd.PIECES_ON_FLY.clear();
         int amount = tag.getInt("bounds");
         for (int j = 0; j < amount; j++) {
            CompoundNBT resource = tag.getCompound(String.valueOf(j));
            csd.PIECES_ON_FLY.add(ResourceData.load(resource));
         }
         Utils.replace(csd, true);
      }
      Utils.onChanged(true);
   }

   @Override
   public CompoundNBT save(CompoundNBT nbt) {
      int i = 0;
      for (CustomStructureData csd : Utils.getStructures(true)) {
         CompoundNBT tag = new CompoundNBT();
         int j = 0;
         for (ResourceData rd : csd.PIECES_ON_FLY) {
            tag.put(String.valueOf(j++), rd.save());
         }
         tag.putString("structure", csd.getName());
         tag.putInt("bounds", j);
         nbt.put(String.valueOf(i++), tag);
      }
      nbt.putInt("structures", i);
      return nbt;
   }
}