/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class VariationConfiguration {
   public static final Codec<VariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
         Codec.BOOL.fieldOf("is_enabled").forGetter(VariationConfiguration::isEnabled),
         SimpleVariationConfiguration.CODEC.optionalFieldOf("simple", SimpleVariationConfiguration.ALL_DISABLED).forGetter(VariationConfiguration::getSimpleVariationConfiguration),
         NestedVariationConfiguration.CODEC.optionalFieldOf("nested", NestedVariationConfiguration.ALL_DISABLED).forGetter(VariationConfiguration::getNestedVariationConfiguration)
   ).apply(variationConfigurationInstance, VariationConfiguration::new));
   public static final VariationConfiguration ALL_ENABLED = new VariationConfiguration(true, SimpleVariationConfiguration.ALL_ENABLED, NestedVariationConfiguration.ALL_ENABLED);
   public static final VariationConfiguration ALL_DISABLED = new VariationConfiguration(false, SimpleVariationConfiguration.ALL_DISABLED, NestedVariationConfiguration.ALL_DISABLED);
   private boolean isEnabled;
   private SimpleVariationConfiguration simpleVariationConfiguration;
   private NestedVariationConfiguration nestedVariationConfiguration;

   public VariationConfiguration(boolean isEnabled, SimpleVariationConfiguration simpleVariationConfiguration, NestedVariationConfiguration nestedVariationConfiguration) {
      this.isEnabled = isEnabled;
      this.simpleVariationConfiguration = simpleVariationConfiguration;
      this.nestedVariationConfiguration = nestedVariationConfiguration;
   }

   public boolean isEnabled() {
      return isEnabled;
   }

   public void setEnabled(boolean enabled) {
      isEnabled = enabled;
   }

   public SimpleVariationConfiguration getSimpleVariationConfiguration() {
      return simpleVariationConfiguration;
   }

   public void setSimpleVariationConfiguration(SimpleVariationConfiguration simpleVariationConfiguration) {
      this.simpleVariationConfiguration = simpleVariationConfiguration;
   }

   public NestedVariationConfiguration getNestedVariationConfiguration() {
      return nestedVariationConfiguration;
   }

   public void setNestedVariationConfiguration(NestedVariationConfiguration nestedVariationConfiguration) {
      this.nestedVariationConfiguration = nestedVariationConfiguration;
   }
}