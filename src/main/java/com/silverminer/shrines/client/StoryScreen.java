/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.client;

import com.silverminer.shrines.network.CTSFetchStoriesPacket;
import com.silverminer.shrines.network.NetworkManager;
import com.silverminer.shrines.stories.Snippet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StoryScreen extends BookViewScreen {
   public static void open() {
      Minecraft.getInstance().setScreen(new ProgressScreen(false));
      NetworkManager.SIMPLE_CHANNEL.sendToServer(new CTSFetchStoriesPacket());
   }

   public StoryScreen(List<List<Snippet>> unlockedStories) {
      super(new BookAccess() {
         @Override
         public int getPageCount() {
            return unlockedStories.size();
         }

         @Override
         public @NotNull FormattedText getPageRaw(int page) {
            StringBuilder stringBuilder = new StringBuilder();
            List<Snippet> snippets = new ArrayList<>(unlockedStories.get(page));
            snippets.sort(Comparator.comparing(Snippet::id));
            for(int i = 0; i < snippets.size(); i++){
               stringBuilder.append("#").append(i + 1).append(" ").append(Component.translatable(snippets.get(i).text()).getString()).append("\n");
            }
            return FormattedText.of(stringBuilder.toString());
         }
      });
   }
}