/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages;

import com.silverminer.shrines.packages.client.ClientStructurePackageManager;
import com.silverminer.shrines.packages.server.ServerStructurePackageManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PackageManagerProvider {
   public static final ServerStructurePackageManager SERVER = new ServerStructurePackageManager();
   public static final ClientStructurePackageManager CLIENT = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientStructurePackageManager::getInstance);
}