/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.proxy;

import java.io.File;

import net.minecraft.client.Minecraft;

public class ClientProxy implements IProxy {

    @Override
    public File getBaseDir() {
        Minecraft mc = Minecraft.getInstance();
        return mc.gameDirectory;
    }
}