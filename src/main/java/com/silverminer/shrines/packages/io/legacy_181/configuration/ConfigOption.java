/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io.legacy_181.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.function.Function;

public class ConfigOption<T> implements IConfigOption<T> {
   protected static final Logger LOGGER = LogManager.getLogger(ConfigOption.class);
   private final String name;
   private T value;
   private final Function<String, T> fromString;
   private final Function<T, String> toString;

   public ConfigOption(String name, T value, Function<String, T> fromString) {
      this(name, value, fromString, String::valueOf);
   }

   public ConfigOption(String name, T value, Function<String, T> fromString, Function<T, String> toString) {
      this.name = name;
      this.value = value;
      this.fromString = fromString;
      this.toString = toString;
   }

   public Function<String, T> getFromString(String option) {
      return fromString;
   }

   public boolean equals(Object o) {
      if (o instanceof IConfigOption) {
         return Objects.equals(((IConfigOption<?>) o).getName(), this.getName());
      } else {
         return false;
      }
   }

   public void setValue(T v, String structure) {
      this.value = v;
   }

   public String toString() {
      return this.name + ":" + this.toString.apply(this.getValue());
   }

   public T getValue() {
      return value;
   }

   public String getName() {
      return this.name;
   }
}