/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.FilledStructureTemplate;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class AddTemplatesScreen extends Screen {
   protected final Screen lastScreen;
   protected final String[] files;
   protected final List<String> invalidFiles;
   protected AddTemplatesList addTemplatesList;
   protected StructuresPackageWrapper packet;
   protected Button saveButton;
   protected Button infoButton;

   protected AddTemplatesScreen(Screen lastScreen, StructuresPackageWrapper packet, String[] files) {
      super(new TranslatableComponent("gui.shrines.templates.add"));
      this.lastScreen = lastScreen;
      this.packet = packet;
      this.files = files;
      this.invalidFiles = Arrays.stream(this.files).filter(AddTemplatesScreen::invalidateTemplate).collect(Collectors.toList());
   }

   /**
    * @return true if the given file doesn't point to a valid template file
    */
   protected static boolean invalidateTemplate(String s) {
      try {
         CompoundTag nbt = NbtIo.read(new File(s));
         if (nbt == null) {
            return true;
         }
         return nbt.getInt("DataVersion") <= 0 || !s.endsWith(".nbt");
      } catch (Throwable t) {
         try {
            CompoundTag nbt = NbtIo.readCompressed(new File(s));
            return nbt.getInt("DataVersion") <= 0 || !s.endsWith(".nbt");
         } catch (Throwable t2) {
            return true;
         }
      }
   }

   public void onClose() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(this.lastScreen);
   }

   protected void init() {
      super.init();
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
      this.saveButton = this.addRenderableWidget(new Button(this.width - 60 - 2, 2, 60, 20, new TranslatableComponent("gui.shrines.save"), (button) -> this.save()));
      this.infoButton = this.addRenderableWidget(new Button(this.width - 60 - 2 - 20 - 2, 2, 20, 20, new TextComponent("?"), (button) -> {
      }, (button, ms, x, y) -> {
         StringBuilder sb = new StringBuilder();
         for (String s : this.invalidFiles) {
            sb.append(s);
            sb.append("\n");
         }
         TextComponent invalidFiles = new TextComponent(sb.toString());
         Component head = new TranslatableComponent("gui.shrines.templates.add.left_out", invalidFiles);
         int i = 0;
         for (String line : head.getString().split("\n")) {
            this.renderTooltip(ms, new TextComponent(line), x, y + i++ * 9);
         }
      }));
      this.infoButton.visible = this.invalidFiles.size() > 0;
      this.addTemplatesList = new AddTemplatesList(this.minecraft, this.width, this.height, 26, this.height, 36, this.packet, this, this.files);
      this.addWidget(this.addTemplatesList);
   }

   public void save() {
      if (this.minecraft != null && this.minecraft.player != null) {
         if (this.addTemplatesList.children().isEmpty()) {
            PackageManagerProvider.CLIENT.onError(new CalculationError("Failed to add new templates", "None of the selected templates has a valid name"));
            return;
         }
         this.addTemplatesList.children().forEach((entry) -> {
            try {
               CompoundTag templateData;
               try {
                  templateData = NbtIo.readCompressed(new File(entry.getPath()));
               } catch (EOFException eofException) {
                  templateData = NbtIo.read(new File(entry.getPath()));
               }
               if (templateData == null) {
                  PackageManagerProvider.CLIENT.onError(new CalculationError("Failed to add new template", "An unknown issue occurred while trying to add new template. Please report this issue"));
               }
               this.packet.getTemplates().add(new FilledStructureTemplate(entry.getLocation(), templateData));
            } catch (IOException e) {
               PackageManagerProvider.CLIENT.onError(new CalculationError("Failed to add new template", "Failed to read file from [%s]. Caused by: %s", entry.getPath(), e.getClass().getName() + ": " + e.getMessage()));
            }
         });
         this.onClose();
      }
   }

   public void toggleSave(boolean active) {
      this.saveButton.active = active;
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
      this.addTemplatesList.render(ms, mouseX, mouseY, p_230430_4_);
      drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(ms, mouseX, mouseY, p_230430_4_);
   }
}