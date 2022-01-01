package com.silverminer.shrines.gui.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class SetNameScreen extends Screen {
   protected final Screen lastScreen;
   private final Component defaultText;
   private final Component info;
   private final Consumer<String> done;
   private final Function<String, Boolean> validator;
   protected EditBox nameField;
   protected Button confirmButton;

   public SetNameScreen(Screen lastScreen, Component title, Component defaultText, Component info, Consumer<String> done) {
      this(lastScreen, title, defaultText, info, done, (value) -> !value.isEmpty());
   }

   public SetNameScreen(Screen lastScreen, Component title, Component defaultText, Component info, Consumer<String> done, Function<String, Boolean> validator) {
      super(title);
      this.lastScreen = lastScreen;
      this.defaultText = defaultText;
      this.info = info;
      this.done = done;
      this.validator = validator;
   }

   public void onClose() {
      if (this.minecraft != null) {
         this.minecraft.setScreen(lastScreen);
      }
   }

   protected void init() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
      String message = this.nameField == null ? this.defaultText.getString() : this.nameField.getValue();
      this.nameField = new EditBox(font, this.width / 2 - 75, this.height / 2 - 40, 150, 20,
            TextComponent.EMPTY);
      this.nameField.setValue(message);
      this.nameField.setResponder(s -> this.confirmButton.active = validator.apply(s));
      this.confirmButton = this.addRenderableWidget(new Button(this.width / 2 - 70, this.height / 2 + 20, 140, 20,
            new TranslatableComponent("gui.shrines.confirm"), (button) -> {
         this.done.accept(this.nameField.getValue());
         this.onClose();
      }));
      this.confirmButton.active = validator.apply(this.nameField.getValue());
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
      this.addWidget(this.nameField);
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack matrixStack, int x, int y, float p_230430_4_) {
      this.renderDirtBackground(0);
      this.nameField.render(matrixStack, x, y, p_230430_4_);
      if (font.width(info) > (this.width / 4) * 3) {
         String sb = "";
         StringBuilder sb1 = new StringBuilder();
         String[] words = info.getString().split(" ");
         for (String word : words) {
            if (font.width(sb) < (this.width / 4) * 3) {
               sb += word + " ";
            } else {
               sb1.append(word).append(" ");
            }
         }
         drawCenteredString(matrixStack, font,
               sb,
               this.width / 2, this.height / 2 - 16, 0x999999);
         drawCenteredString(matrixStack, font,
               sb1.toString(),
               this.width / 2, this.height / 2 - 4, 0x999999);
      } else {
         drawCenteredString(matrixStack, font,
               info.getString(),
               this.width / 2, this.height / 2 - 16, 0x999999);
      }
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
      super.render(matrixStack, x, y, p_230430_4_);
   }
}