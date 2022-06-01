/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.config;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.util.List;

public class Config {
   @ExpectPlatform
   public static List<String> removedStructures() {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static void setRemovedStructures(List<String> value) {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static List<String> disabledStructures() {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static void setDisabledStructures(List<String> value) {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static boolean runStructureUpdater() {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static void setRunStructureUpdater(boolean value) {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static int minStructureDistance() {
      throw new AssertionError();
   }

   @ExpectPlatform
   public static void setMinStructureDistance(int value) {
      throw new AssertionError();
   }
}