/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.silverminer.shrines.stories.Snippet;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.settings.KeyConflictContext;

import java.util.List;

public class ClientUtils {
   public static void onUnlockSnippet(ResourceLocation storyID, String snippetText){
      Minecraft.getInstance().getToasts().addToast(StoryToast.create(Minecraft.getInstance(), storyID, snippetText));
   }

   public static void openStoriesScreen(List<List<Snippet>> unlockedStories){
      Minecraft.getInstance().setScreen(StoryScreen.create(unlockedStories));
   }

   public static class KeyMappings {
      public static final KeyMapping openStoryScreen = new KeyMapping("key.shrines.stories", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_K, "key.shrines.categories.stories");
   }
}