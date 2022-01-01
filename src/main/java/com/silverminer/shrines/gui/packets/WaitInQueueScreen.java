package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.ColorLoop;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WaitInQueueScreen extends Screen implements SkipableScreen {
   private final ColorLoop loop = new ColorLoop();
   private final Screen lastScreen;

   public WaitInQueueScreen(Screen lastScreen) {
      super(new TranslatableComponent("gui.shrines.queue"));
      this.lastScreen = lastScreen;
   }

   public void onClose() {
      PackageManagerProvider.CLIENT.leaveQueue();
      this.minecraft.setScreen(this.lastScreen);
   }

   protected void init() {
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> {
         this.onClose();
      }, TextComponent.EMPTY));
   }

   public void render(PoseStack matrixStack, int x, int y, float p_230430_4_) {
      this.loop.tick();
      this.renderDirtBackground(0);
      drawCenteredString(matrixStack, this.font,
            new TranslatableComponent(
                  "gui.shrines.queue.info", PackageManagerProvider.CLIENT.getQueuePosition()),
            this.width / 2, this.height / 2, 0xffffff);
      drawCenteredString(matrixStack, this.font,
            new TranslatableComponent("gui.shrines.queue.patient")
                  .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(this.loop.getRGB()))),
            this.width / 2, this.height / 2 + 15, 0xffffff);
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
      super.render(matrixStack, x, y, p_230430_4_);
   }

   public Screen getLastScreen() {
      return lastScreen;
   }
}
