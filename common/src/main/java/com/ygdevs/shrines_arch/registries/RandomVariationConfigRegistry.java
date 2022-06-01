/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.ygdevs.shrines_arch.random_variation.RandomVariationConfig;
import com.ygdevs.shrines_arch.registry.ModRegistrar;

public class RandomVariationConfigRegistry {
   public static final ModRegistrar<RandomVariationConfig> REGISTRY = ModRegistrar.create("minecraft", RandomVariationConfig.REGISTRY);
}