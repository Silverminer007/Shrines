package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.packages.datacontainer.StructureTemplate;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class RenameTemplateScreen extends Screen {
   protected final Screen lastScreen;
   protected final StructureTemplate template;
   protected final StructuresPackageWrapper packet;
   protected Component info;
   protected EditBox nameField;
   protected Button confirmButton;

   protected RenameTemplateScreen(Screen lastScreen, StructureTemplate template, StructuresPackageWrapper packet) {
      super(new TranslatableComponent("gui.shrines.packets.add"));
      this.packet = packet;
      this.lastScreen = lastScreen;
      this.template = template;
   }

   public void onClose() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(lastScreen);
   }

   protected void init() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
      String message = this.nameField == null ? this.template.toString() : this.nameField.getValue();
      this.nameField = new EditBox(font, this.width / 2 - 75, this.height / 2 - 40, 150, 20,
            TextComponent.EMPTY);
      this.nameField.setValue(message);
      this.nameField.setResponder(this::updateSaveButton);
      this.confirmButton = this.addRenderableWidget(new Button(this.width / 2 - 70, this.height / 2 + 20, 140, 20,
            new TranslatableComponent("gui.shrines.confirm"), (button) -> this.done()));
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
      this.addWidget(this.nameField);
      this.updateSaveButton(this.nameField.getValue());
   }

   private void updateSaveButton(String newName) {
      try {
         // That expression looks a bit strange at first, but I strip the extension .nbt, and then I add it again to prevent double extensions if there was already one
         this.confirmButton.active = packet.getTemplates().getAsStream().map(Object::toString).map(temp -> temp.replace(".nbt", "")).noneMatch(temp -> temp.equals(new ResourceLocation(newName).toString().replace(".nbt", "")));
         if (this.confirmButton.active) {
            this.info = TextComponent.EMPTY;
         } else {
            this.info = new TranslatableComponent("gui.shrines.templates.rename.name_taken");
         }
      } catch (ResourceLocationException e) {
         this.confirmButton.active = false;
         this.info = new TranslatableComponent("gui.shrines.templates.rename.invalid_name");
      }
   }

   private void done() {
      this.template.setTemplateLocation(new ResourceLocation(this.nameField.getValue()));
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack matrixStack, int x, int y, float p_230430_4_) {
      this.renderDirtBackground(0);
      this.nameField.render(matrixStack, x, y, p_230430_4_);
      drawCenteredString(matrixStack, this.font, new TranslatableComponent("gui.shrines.templates.rename.old_name", this.template.toString()), this.width / 2, this.height / 2 - 52, 0xcdcdcd);
      drawCenteredString(matrixStack, this.font, this.info, this.width / 2, this.height / 2 - 10, 0xaa0000);
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
      super.render(matrixStack, x, y, p_230430_4_);
   }
}