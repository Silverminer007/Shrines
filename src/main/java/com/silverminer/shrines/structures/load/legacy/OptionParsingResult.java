/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.load.legacy;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class OptionParsingResult {
   private final boolean success;
   private ITextComponent message;

   public OptionParsingResult(boolean success, ITextComponent message) {
      this.success = success;
      this.message = message;
   }

   public ITextComponent getMessage() {
      if (this.message != null)
         return message;
      else
         return new StringTextComponent("");
   }

   public OptionParsingResult setMessage(ITextComponent message) {
      this.message = message;
      return this;
   }

   public boolean isSuccess() {
      return success;
   }
}