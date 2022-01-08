/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.proxy;

import java.io.File;

import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeServerProxy implements IProxy {

   @Override
   public File getBaseDir() {
      return FMLPaths.GAMEDIR.get().toFile();
   }
}