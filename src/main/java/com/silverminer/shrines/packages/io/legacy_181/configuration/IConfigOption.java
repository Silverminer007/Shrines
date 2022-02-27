/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io.legacy_181.configuration;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Random;
import java.util.function.Function;

/**
 * @author Silverminer
 */
public interface IConfigOption<T> {

   T getValue();

   String getName();

   default OptionParsingResult fromString(String s, IStructureConfig data, boolean set) {
      if (data == null) {
         return new OptionParsingResult(false, new TextComponent("Failed to read structure from string, because config instance was null"));
      }
      if (s == null || s.replaceAll(" ", "").isEmpty()) {
         return new OptionParsingResult(false, null);
      }
      T v;
      if (s.startsWith(getName() + ":")) {
         v = this.getFromString(this.getName()).apply(s.replace(this.getName() + ":", ""));
      } else {
         v = this.getFromString(this.getName()).apply(s);
      }
      if (v == null) {
         return new OptionParsingResult(false,
               new TranslatableComponent("commands.shrines.configure.failed.wrong_value", s, this.getName()));
      } else if (this.getName().equals("distance")) {
         if (((Integer) v) <= data.getSeparation()) {
            return new OptionParsingResult(false, new TranslatableComponent(
                  "commands.shrines.configure.failed.dist_larger_sep", v, data.getSeparation()));
         }
      } else if (this.getName().equals("seperation")) {
         if (((Integer) v) >= data.getDistance()) {
            return new OptionParsingResult(false, new TranslatableComponent(
                  "commands.shrines.configure.failed.sep_smaller_dist", v, data.getDistance()));
         }
      } else if (this.getName().equals("spawn_chance")) {
         double d = (Double) v;
         if (d < 0.0 || d > 1.0) {
            return new OptionParsingResult(false,
                  new TranslatableComponent("commands.shrines.configure.failed.chance_out_of_range", v));
         }
      } else if (this.getName().equals("seed")) {
         int i = (Integer) v;
         if (i < 0) {
            return this.fromString(String.valueOf(i * -1), data, set).setMessage(
                  new TranslatableComponent("commands.shrines.configure.failed.seed_set_positive"));
         } else if (i == 0) {
            return this.fromString(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)), data, set)
                  .setMessage(new TranslatableComponent("commands.shrines.configure.failed.seed_set_random"));
         }
      }
      if (set) {
         this.setValue(v, data.getName());
      }
      return new OptionParsingResult(true, null);
   }

   Function<String, T> getFromString(String option);

   void setValue(T v, String structure);

   default OptionParsingResult fromString(String text, IStructureConfig configSpecs) {
      return this.fromString(text, configSpecs, true);
   }
}