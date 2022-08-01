/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.network.CTSFetchStoriesPacket;
import com.silverminer.shrines.network.NetworkManager;
import com.silverminer.shrines.stories.Snippet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StoryScreen extends BookViewScreen {
   public static void open() {
      Minecraft.getInstance().setScreen(new ProgressScreen(false));
      NetworkManager.SIMPLE_CHANNEL.sendToServer(new CTSFetchStoriesPacket());
   }

   protected StoryScreen(List<FormattedText> pages) {
      super(new BookAccess() {
         @Override
         public int getPageCount() {
            return pages.size();
         }

         @Override
         public @NotNull FormattedText getPageRaw(int page) {
            return pages.get(page);
         }
      });
   }

   @Contract("_ -> new")
   public static @NotNull StoryScreen create(@NotNull List<List<Snippet>> unlockedStories) {
      List<FormattedText> pages = new ArrayList<>();
      for (List<Snippet> unlockedStory : unlockedStories) {
         List<Snippet> snippets = new ArrayList<>(unlockedStory);
         snippets.sort(Comparator.comparing(Snippet::id));
         for (Snippet snippet : snippets) {
            pages.add(FormattedText.of("#" + snippet.id() + " " + Component.translatable(snippet.text()).getString() + "\n"));
         }
      }
      return new StoryScreen(pages);
   }
}