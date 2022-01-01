package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.misc.SetNameScreen;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageInfo;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class StructuresPacketsScreen extends Screen {
   protected static final Logger LOGGER = LogManager.getLogger(StructuresPacketsScreen.class);
   protected final Screen lastScreen;
   protected EditBox searchBox;
   protected StructurePacketsList list;
   protected Button delete;
   protected Button configure;
   protected Button add;
   protected Button rename;
   protected Button export;

   public StructuresPacketsScreen(Screen lastScreen) {
      super(new TranslatableComponent("gui.shrines.packets"));
      this.lastScreen = lastScreen;
   }

   public void onClose() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(new DirtConfirmScreen((result) -> {
         if (result) {
            this.close();
         } else {
            this.minecraft.setScreen(this);
         }
      }, new TranslatableComponent("Are you sure that you want to exit without saving?"),// TRANSLATION
            new TranslatableComponent("All unsaved changes will be lost forever"),
            new TranslatableComponent("Leave"),
            new TranslatableComponent("Back")));
   }

   public void save() {
      if (this.minecraft != null) {
         this.minecraft.setScreen(new WorkingScreen(this.lastScreen, this, new TranslatableComponent("gui.shrines.saving"), new TranslatableComponent("gui.shrines.saving.info")));// TRANSLATION
         PackageManagerProvider.CLIENT.savePackages();
      }
   }

   private void close() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(this.lastScreen);
      PackageManagerProvider.CLIENT.stopEditing();
   }

   protected void init() {
      if (this.minecraft == null || this.minecraft.player == null) {
         return;
      }
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
      this.searchBox = new EditBox(this.font, (this.width / 4) * 3 - 25, 3, 100, 20, this.searchBox,
            new TextComponent(""));
      this.searchBox.setResponder((string) -> this.list.refreshList(() -> string));
      this.list = new StructurePacketsList(this, this.minecraft, this.width, this.height, 26, this.height - 48, 36,
            () -> this.searchBox.getValue());

      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20,
            ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));

      this.addRenderableWidget(new Button(95, 2, 60, 20,
            new TranslatableComponent("gui.shrines.save"), (button) -> this.save()));// TRANSLATION

      this.delete = this.addRenderableWidget(new Button(this.width / 2 - 80 - 80 - 9, this.height - 45, 80, 20,
            new TranslatableComponent("gui.shrines.delete"), (button) -> this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::remove)));

      this.configure = this.addRenderableWidget(new Button(this.width / 2 - 80 - 3, this.height - 45, 80, 20,
            new TranslatableComponent("gui.shrines.configure"), (button) -> this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::configure)));

      this.add = this.addRenderableWidget(new Button(this.width / 2 + 3, this.height - 22, 166, 20,
            new TranslatableComponent("gui.shrines.add"), (button) -> this.minecraft.setScreen(new SetNameScreen(this,
            new TranslatableComponent("gui.shrines.packets.add.enter_name"),
            TextComponent.EMPTY,
            new TranslatableComponent("gui.shrines.packets.add.info"),
            (value) -> // Called when the confirmed button was pressed, before screen is closed
                  PackageManagerProvider.CLIENT.getPackages().add(new StructuresPackageWrapper(new StructuresPackageInfo(value, this.minecraft.player.getName().getString()))),
            (value) -> // Validates the current input
                  !value.isBlank()))));// Duplicates names aren't clean, but we shouldn't care because it doesn't matter for saving

      this.rename = this.addRenderableWidget(new Button(this.width / 2 + 3, this.height - 45, 80, 20,
            new TranslatableComponent("gui.shrines.rename"), (button) -> this.list.getSelectedOpt().ifPresent(entry -> this.minecraft
            .setScreen(new SetNameScreen(this,
                  new TranslatableComponent("gui.shrines.packets.rename.enter_name"),
                  new TranslatableComponent(entry.getPacket().getStructuresPacketInfo().getDisplayName()),
                  new TranslatableComponent("gui.shrines.packets.rename.info"),
                  (value) -> entry.getPacket().getStructuresPacketInfo().setDisplayName(value))))));

      this.addRenderableWidget(new Button((this.width / 4) * 3 + 79, 3, 40, 20,
            new TranslatableComponent("gui.shrines.help"),
            (button) -> this.handleComponentClicked(Style.EMPTY.withClickEvent(
                  new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Silverminer007/Shrines/wiki")))));
      this.addRenderableWidget(new Button(this.width / 2 - 80 - 80 - 9, this.height - 22, 166, 20,
            new TranslatableComponent("gui.shrines.import"), (button) -> this.importPackage()));// TRANSLATION gui.shrines.import.legacy remove key
      this.export = this.addRenderableWidget(new Button(this.width / 2 + 80 + 9, this.height - 45, 80, 20,
            new TranslatableComponent("gui.shrines.export"), (button) -> this.list.getSelectedOpt().ifPresent(entry -> this.exportPackage(entry.getPacket()))));
      this.updateButtonStatus(false);
      this.addWidget(this.searchBox);
      this.addWidget(this.list);
      this.setInitialFocus(this.searchBox);
   }

   public void importPackage() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(new DirtConfirmScreen((result) -> {
         if (result) {
            PackageManagerProvider.CLIENT.importPackage();
         } else {
            this.minecraft.setScreen(this);
         }
      }, new TranslatableComponent("Do you want to save changes?"),// TRANSLATION
            new TranslatableComponent("You need to save changes before you can import packages"),
            new TranslatableComponent("Save"),
            new TranslatableComponent("Back")));
   }

   public void exportPackage(StructuresPackageWrapper structuresPackageWrapper) {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(new DirtConfirmScreen((result) -> {
         if (result) {
            PackageManagerProvider.CLIENT.exportPackage(structuresPackageWrapper);
         } else {
            this.minecraft.setScreen(this);
         }
      }, new TranslatableComponent("Do you want to save changes?"),// TRANSLATION
            new TranslatableComponent("You need to save changes before you can export packages"),
            new TranslatableComponent("Save"),
            new TranslatableComponent("Back")));
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack matrixStack, int x, int y, float p_230430_4_) {
      this.list.render(matrixStack, x, y, p_230430_4_);
      this.searchBox.render(matrixStack, x, y, p_230430_4_);
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(matrixStack, x, y, p_230430_4_);
   }

   public void updateButtonStatus(boolean hasSelected) {
      this.delete.active = hasSelected;
      this.configure.active = hasSelected;
      this.rename.active = hasSelected;
      this.export.active = hasSelected;
   }
}