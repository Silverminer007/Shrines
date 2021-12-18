package com.silverminer.shrines.gui.novels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSPlayerJoinedQueuePacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.Slider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class StructureNovelsScreen extends Screen {
   protected final Screen lastScreen;
   private final ArrayList<StructuresPacket> packets;
   protected EditBox searchBox;
   protected Slider sizeSlider;
   private StructureNovelsList list;
   private boolean isExpanded = false;
   private int itemSize = 50;

   public StructureNovelsScreen(Screen lastScreen, ArrayList<StructuresPacket> packets) {
      super(new TranslatableComponent("gui.shrines.novels.title"));
      this.lastScreen = lastScreen;
      this.packets = packets;
   }

   protected void init() {
      if (this.minecraft == null || this.minecraft.player == null) {
         return;
      }
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
      this.searchBox = new EditBox(this.font, (this.width / 4) * 3 - 20, 3, 100, 20, this.searchBox,
            new TextComponent(""));
      this.searchBox.setResponder((string) -> this.list.refreshList(() -> string, this.packets));
      this.list = new StructureNovelsList(this.minecraft, this.width, this.height, this.isExpanded ? 52 : 26,
            this.height, this.itemSize, () -> this.searchBox
            .getValue(), packets);
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20,
            ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
      // Slider
      double sliderValue = sizeSlider == null ? -1 : Mth.floor(Mth.clampedLerp(50.0D, 150.0D, sizeSlider.sliderValue));
      sizeSlider = this.addRenderableWidget(new Slider((this.width / 4) * 3 - 20, 29, 100, 20,
            new TranslatableComponent("gui.shrines.novels.item_size"), new TextComponent(""),
            50.0D, 150.0D, 100.0, false, true, (slider) -> {
      }, slider -> {
         StructureNovelsScreen.this.itemSize = Mth.floor(Mth.clampedLerp(50.0D, 150.0D, slider.sliderValue));
         this.list = new StructureNovelsList(this.minecraft, this.width, this.height, this.isExpanded ? 52 : 26,
               this.height, this.itemSize, () -> this.searchBox
               .getValue(), packets);
      }) {
      });
      sizeSlider.setValue(sliderValue == -1 ? sizeSlider.sliderValue : sliderValue);
      sizeSlider.updateSlider();
      sizeSlider.visible = this.isExpanded;
      // Slider End
      Button opMode = this.addRenderableWidget(new Button((this.width / 2) - 75, 29, 150, 20,
            new TranslatableComponent("gui.shrines.open_admin_mode"), (button) -> this.addPlayerToQueue()));
      opMode.active = this.minecraft.player.hasPermissions(2);
      opMode.visible = this.isExpanded;
      Button expand = this.addRenderableWidget(new Button((this.width / 4) * 3 + 90, 4, 20, 20,
            new TextComponent(this.isExpanded ? "^" : "˅"), (button) -> {
         this.isExpanded = !this.isExpanded;
         this.minecraft.setScreen(this);
      }));
      expand.visible = opMode.visible;
      this.addWidget(this.searchBox);
      this.addWidget(this.list);
      this.setInitialFocus(this.searchBox);
   }

   public void addPlayerToQueue() {
      if (this.minecraft == null || this.minecraft.player == null) {
         return;
      }
      this.minecraft.setScreen(new ProgressScreen(true));
      ShrinesPacketHandler.sendToServer(new CTSPlayerJoinedQueuePacket());
   }

   public void refreshList() {
      this.list.refreshList(() -> this.searchBox.getValue(), this.packets);
   }

   public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
      return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_) || this.searchBox.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
   }

   public void onClose() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(this.lastScreen);
   }

   public boolean charTyped(char p_231042_1_, int p_231042_2_) {
      return this.searchBox.charTyped(p_231042_1_, p_231042_2_);
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack matrixStack, int x, int y, float p_230430_4_) {
      this.list.render(matrixStack, x, y, p_230430_4_);
      this.searchBox.render(matrixStack, x, y, p_230430_4_);
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(matrixStack, x, y, p_230430_4_);
   }
}