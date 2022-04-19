/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.test;

import com.silverminer.shrines.Shrines;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.NotNull;

@GameTestHolder(Shrines.MODID)
public class DataPackRegistryTest {
   @PrefixGameTestTemplate(false)
   @GameTest(templateNamespace = Shrines.MODID, template = "gameteststructures/empty")
   public static void testElementPresence(@NotNull GameTestHelper helper) {
      helper.succeedOnTickWhen(0, () -> {
         try {
            Test testA = TestRegistry.TEST_A.get();
            Test testB = TestRegistry.TEST_B.get();
            Test testC = TestRegistry.TEST_C.get();
            if (testA == null) {
               throw new GameTestAssertException("Test A failed to load {" + TestRegistry.TEST_A.key().location() + "}");
            }
            if (testB == null) {
               throw new GameTestAssertException("Test B failed to load {" + TestRegistry.TEST_B.key().location() + "}");
            }
            if (testC == null) {
               throw new GameTestAssertException("Test C failed to load {" + TestRegistry.TEST_C.key().location() + "}");
            }
            if (!testC.string().equals("")) {
               throw new GameTestAssertException("Test C override failed");
            }
            if (!testA.string().equals("goodbye") || testA.number() != 42) {
               throw new GameTestAssertException("Test A was overridden, but it shouldn't");
            }
         } catch (GameTestAssertException e) {
            throw e;
         } catch (Exception e) {
            throw new GameTestAssertException("Test Registry failed to load. Caused by: " + e);
         }
      });
   }
}