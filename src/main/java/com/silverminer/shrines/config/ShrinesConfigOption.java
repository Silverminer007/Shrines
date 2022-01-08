/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.config;

import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author Silverminer
 */
public class ShrinesConfigOption<T> implements IConfigOption<T> {
   protected static final Logger LOGGER = LogManager.getLogger(ShrinesConfigOption.class);
   public final ForgeConfigSpec.ConfigValue<T> option;
   public final T defaultValue;

   /**
    * @param parent
    * @param path
    * @param defaultSupplier
    */
   public ShrinesConfigOption(ForgeConfigSpec.ConfigValue<T> option, T defaultValue) {
      this.option = option;
      this.defaultValue = defaultValue;
   }

   @Override
   public T getValue() {
      return this.option.get();
   }

   @Override
   public String getName() {
      List<String> path = this.option.getPath();
      return path.get(path.size() - 1);
   }

   @Override
   public T getDefaultValue() {
      return this.defaultValue;
   }

   @Override
   public void setValue(T v, String structure) {
      this.option.set(v);
      this.option.save();
   }

   @SuppressWarnings("unchecked")
   @Override
   public Function<String, T> getFromString(String option) {
      CustomStructureData accessor = new CustomStructureData();
      for (IConfigOption<?> co : accessor.getAllOptions()) {
         if (co.getName().equals(option)) {
            return (Function<String, T>) co.getFromString(option);
         }
      }
      return this::getValueFromString;
   }

   @SuppressWarnings("unchecked")
   public T getValueFromString(String s) {
      T value = this.getValue();
      if (value instanceof Double) {
         return (T) Double.valueOf(s);
      }
      if (value instanceof Boolean) {
         return (T) Boolean.valueOf(s);
      }
      LOGGER.info("Unable to find parser for: {}, {}", s, value.getClass());
      return value;
   }
}