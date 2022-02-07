/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.config;

import java.util.stream.Collectors;

import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.custom.CustomStructure;

import net.minecraftforge.common.ForgeConfigSpec;

public class StructureConfig {

    public StructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
        for (AbstractStructure<?> structure : NewStructureInit.STRUCTURES.values().stream()
                .filter(struct -> !(struct instanceof CustomStructure)).collect(Collectors.toList())) {
            structure.buildConfig(SERVER_BUILDER);
        }
    }
}