/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.functions;

import java.util.List;

import net.minecraft.block.Block;

/**
 * @author Silverminer
 */
public interface IFunctionProvider {
   List<String> getBiomes();

   Block getBlockByID(String ID);
}