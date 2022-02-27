/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class NestedVariationConfiguration {
   public static final Codec<NestedVariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
         Codec.BOOL.fieldOf("are_slabs_enabled").forGetter(NestedVariationConfiguration::isAreSlabsEnabled),
         Codec.BOOL.fieldOf("is_button_enabled").forGetter(NestedVariationConfiguration::isButtonEnabled),
         Codec.BOOL.fieldOf("is_fence_enabled").forGetter(NestedVariationConfiguration::isFenceEnabled),
         Codec.BOOL.fieldOf("are_normal_logs_enabled").forGetter(NestedVariationConfiguration::isAreNormalLogsEnabled),
         Codec.BOOL.fieldOf("are_stripped_logs_enabled").forGetter(NestedVariationConfiguration::isAreStrippedLogsEnabled),
         Codec.BOOL.fieldOf("are_trapdoors_enabled").forGetter(NestedVariationConfiguration::isAreTrapdoorsEnabled),
         Codec.BOOL.fieldOf("are_doors_enabled").forGetter(NestedVariationConfiguration::isAreDoorsEnabled),
         Codec.BOOL.fieldOf("is_stair_enabled").forGetter(NestedVariationConfiguration::isStairEnabled),
         Codec.BOOL.fieldOf("is_standing_sign_enabled").forGetter(NestedVariationConfiguration::isStandingSignEnabled),
         Codec.BOOL.fieldOf("is_wall_sign_enabled").forGetter(NestedVariationConfiguration::isWallSignEnabled)
   ).apply(variationConfigurationInstance, NestedVariationConfiguration::new));
   public static final NestedVariationConfiguration ALL_ENABLED = new NestedVariationConfiguration(true, true, true, true, true, true, true, true, true, true);
   public static final NestedVariationConfiguration ALL_DISABLED = new NestedVariationConfiguration(false, false, false, false, false, false, false, false, false, false);
   private boolean areSlabsEnabled;
   private boolean isButtonEnabled;
   private boolean isFenceEnabled;
   private boolean areNormalLogsEnabled;
   private boolean areStrippedLogsEnabled;
   private boolean areTrapdoorsEnabled;
   private boolean areDoorsEnabled;
   private boolean isStairEnabled;
   private boolean isStandingSignEnabled;
   private boolean isWallSignEnabled;

   public NestedVariationConfiguration(boolean areSlabsEnabled, boolean isButtonEnabled, boolean isFenceEnabled, boolean areNormalLogsEnabled, boolean areStrippedLogsEnabled, boolean areTrapdoorsEnabled, boolean areDoorsEnabled, boolean isStairEnabled, boolean isStandingSignEnabled, boolean isWallSignEnabled) {
      this.areSlabsEnabled = areSlabsEnabled;
      this.isButtonEnabled = isButtonEnabled;
      this.isFenceEnabled = isFenceEnabled;
      this.areNormalLogsEnabled = areNormalLogsEnabled;
      this.areStrippedLogsEnabled = areStrippedLogsEnabled;
      this.areTrapdoorsEnabled = areTrapdoorsEnabled;
      this.areDoorsEnabled = areDoorsEnabled;
      this.isStairEnabled = isStairEnabled;
      this.isStandingSignEnabled = isStandingSignEnabled;
      this.isWallSignEnabled = isWallSignEnabled;
   }

   public boolean isAreSlabsEnabled() {
      return areSlabsEnabled;
   }

   public void setAreSlabsEnabled(boolean areSlabsEnabled) {
      this.areSlabsEnabled = areSlabsEnabled;
   }

   public boolean isButtonEnabled() {
      return isButtonEnabled;
   }

   public void setButtonEnabled(boolean buttonEnabled) {
      isButtonEnabled = buttonEnabled;
   }

   public boolean isFenceEnabled() {
      return isFenceEnabled;
   }

   public void setFenceEnabled(boolean fenceEnabled) {
      isFenceEnabled = fenceEnabled;
   }

   public boolean isAreNormalLogsEnabled() {
      return areNormalLogsEnabled;
   }

   public void setAreNormalLogsEnabled(boolean areNormalLogsEnabled) {
      this.areNormalLogsEnabled = areNormalLogsEnabled;
   }

   public boolean isAreStrippedLogsEnabled() {
      return areStrippedLogsEnabled;
   }

   public void setAreStrippedLogsEnabled(boolean areStrippedLogsEnabled) {
      this.areStrippedLogsEnabled = areStrippedLogsEnabled;
   }

   public boolean isAreTrapdoorsEnabled() {
      return areTrapdoorsEnabled;
   }

   public void setAreTrapdoorsEnabled(boolean areTrapdoorsEnabled) {
      this.areTrapdoorsEnabled = areTrapdoorsEnabled;
   }

   public boolean isAreDoorsEnabled() {
      return areDoorsEnabled;
   }

   public void setAreDoorsEnabled(boolean areDoorsEnabled) {
      this.areDoorsEnabled = areDoorsEnabled;
   }

   public boolean isStairEnabled() {
      return isStairEnabled;
   }

   public void setStairEnabled(boolean stairEnabled) {
      isStairEnabled = stairEnabled;
   }

   public boolean isStandingSignEnabled() {
      return isStandingSignEnabled;
   }

   public void setStandingSignEnabled(boolean standingSignEnabled) {
      isStandingSignEnabled = standingSignEnabled;
   }

   public boolean isWallSignEnabled() {
      return isWallSignEnabled;
   }

   public void setWallSignEnabled(boolean wallSignEnabled) {
      isWallSignEnabled = wallSignEnabled;
   }

   public List<String> getDisabledTypes() {
      List<String> disabledTypes = new ArrayList<>();
      if(this.isAreSlabsEnabled()) {
         disabledTypes.add("slab");
      }
      if(this.isButtonEnabled()) {
         disabledTypes.add("button");
      }
      if(this.isFenceEnabled()) {
         disabledTypes.add("fence");
      }
      if(this.isAreNormalLogsEnabled()) {
         disabledTypes.add("log");
      }
      if(this.isAreStrippedLogsEnabled()) {
         disabledTypes.add("stripped_log");
      }
      if(this.isAreTrapdoorsEnabled()) {
         disabledTypes.add("trapdoor");
      }
      if(this.isAreDoorsEnabled()) {
         disabledTypes.add("door");
      }
      if(this.isStairEnabled()) {
         disabledTypes.add("stair");
      }
      if(this.isStandingSignEnabled()) {
         disabledTypes.add("standing_sign");
      }
      if(this.isWallSignEnabled()) {
         disabledTypes.add("wall_sign");
      }
      return disabledTypes;
   }
}