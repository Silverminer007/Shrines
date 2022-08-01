/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.test;

import com.mojang.logging.LogUtils;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.network.NetworkManager;
import com.silverminer.shrines.registries.StructureRegistry;
import com.silverminer.shrines.structures.StructureUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

@GameTestHolder(Shrines.MODID)
public class Tests {
   private static final Logger LOGGER = LogUtils.getLogger();

   @PrefixGameTestTemplate(false)
   @GameTest(templateNamespace = Shrines.MODID, template = "empty")
   public static void testAcceptVanilla(@NotNull GameTestHelper helper) {
      helper.succeedIf(() -> {
         LOGGER.info("Test Vanilla Client Acceptance: Start");
         if (!NetworkRegistry.acceptsVanillaClientConnections()) {
            throw new GameTestAssertException("(0) Shrines doesn't accept Vanilla clients, but it should!");
         }
         LOGGER.info("Test Vanilla Client Acceptance: Success");
         LOGGER.info("Test Vanilla Server Acceptance: Start");
         if (!NetworkRegistry.canConnectToVanillaServer()) {
            throw new GameTestAssertException("(1) Shrines doesn't accept Vanilla servers, but it should!");
         }
         LOGGER.info("Test Vanilla Server Acceptance: Success");
      });
   }

   @PrefixGameTestTemplate(false)
   @GameTest(templateNamespace = Shrines.MODID, template = "empty")
   public static void testChannelVersionTester(@NotNull GameTestHelper helper) {
      helper.succeedIf(() -> {
         if (NetworkManager.checkVersionMatch("1.0", "2.0", true)) {
            throw new GameTestAssertException("(0) Protocol version 1.0 isn't compatible with 2.0");
         }

         if (NetworkManager.checkVersionMatch("2.0", "1.0", true)) {
            throw new GameTestAssertException("(1) Protocol version 1.0 isn't compatible with 2.0");
         }

         if (NetworkManager.checkVersionMatch("1.0", "2.0", false)) {
            throw new GameTestAssertException("(2) Protocol version 1.0 isn't compatible with 2.0");
         }

         if (NetworkManager.checkVersionMatch("2.0", "1.0", false)) {
            throw new GameTestAssertException("(3) Protocol version 1.0 isn't compatible with 2.0");
         }

         if (NetworkManager.checkVersionMatch("1.0", "2.0", true) != NetworkManager.checkVersionMatch("1.0", "2.0", false)) {
            throw new GameTestAssertException("(4) Side shouldn't matter for major version");
         }

         if (NetworkManager.checkVersionMatch("1.0", "1.5", false)) {
            throw new GameTestAssertException("(5) Invalidly accepted invalid version from server. Major matched and minor was newer on client");
         }

         if (!NetworkManager.checkVersionMatch("1.5", "1.0", false)) {
            throw new GameTestAssertException("(6) Invalidly rejected valid version from server. Major matched and minor was newer on client");
         }

         if (!NetworkManager.checkVersionMatch("1.5", "1.5", false)) {
            throw new GameTestAssertException("(7) Invalidly rejected valid version from server. Version matched");
         }

         if (!NetworkManager.checkVersionMatch("1.0", "1.5", true)) {
            throw new GameTestAssertException("(8) Invalidly rejected valid version from client. Major matched and minor was newer on client");
         }

         if (NetworkManager.checkVersionMatch("1.5", "1.0", true)) {
            throw new GameTestAssertException("(9) Invalidly accepted invalid version from client. Major matched and minor was newer on client");
         }

         if (!NetworkManager.checkVersionMatch("1.5", "1.5", true)) {
            throw new GameTestAssertException("(10) Invalidly rejected valid version from server. Version matched");
         }
      });
   }

   @PrefixGameTestTemplate(false)
   @GameTest(templateNamespace = Shrines.MODID, template = "empty")
   public static void testDisableStructures(@NotNull GameTestHelper helper) {
      RegistryAccess registryAccess = helper.getLevel().registryAccess();
      helper.succeedIf(() -> {
         List<String> disabledStructures = List.of(StructureRegistry.ABANDONED_VILLA.getId().toString(), StructureRegistry.BALLOON.getId().toString(), "#" + StructureTags.RUINED_PORTAL.location());

         if (!StructureUtils.invalidateStructure(registryAccess, StructureRegistry.ABANDONED_VILLA.getKey(), disabledStructures)) {
            throw new GameTestAssertException("(0) Abandoned Villa passed structure validation, even tho it is disabled");
         }

         if (!StructureUtils.invalidateStructure(registryAccess, StructureRegistry.BALLOON.getKey(), disabledStructures)) {
            throw new GameTestAssertException("(1) Balloon passed structure validation, even tho it is disabled");
         }

         if (StructureUtils.invalidateStructure(registryAccess, StructureRegistry.WORLD_TREE_MANOR.getKey(), disabledStructures)) {
            throw new GameTestAssertException("(2) World Tree Manor didn't pass structure validation, even tho it is enabled");
         }

         if (!StructureUtils.invalidateStructure(registryAccess, BuiltinStructures.RUINED_PORTAL_DESERT, disabledStructures)) {
            throw new GameTestAssertException("(3) Ruined Portal Desert did pass structure validation, even tho it is disabled");
         }

         ShrinesConfig.disabledStructures.set(List.of());
      });
   }
}
