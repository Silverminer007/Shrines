package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public abstract class NameStructurePacketScreen extends Screen {
    protected final Screen lastScreen;
    protected TextFieldWidget nameField;
    protected Button confirmButton;

    protected NameStructurePacketScreen(Screen lastScreen) {
        super(new TranslationTextComponent("Add Structures packet"));// TRANSLATION
        this.lastScreen = lastScreen;
    }

    public void onClose() {
        this.minecraft.setScreen(lastScreen);
    }

    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
        String message = this.nameField == null ? this.defaultNameFieldString().getString() : this.nameField.getValue();
        this.nameField = new TextFieldWidget(font, this.width / 2 - 75, this.height / 2 - 40, 150, 20,
                StringTextComponent.EMPTY);
        this.nameField.setValue(message);
        this.nameField.setResponder(s -> this.confirmButton.active = !s.replaceAll(" ", "").isEmpty());
        this.confirmButton = this.addButton(new Button(this.width / 2 - 70, this.height / 2 + 20, 140, 20,
                new TranslationTextComponent("Confirm"), (button) -> this.done()));// TRANSLATION
        this.confirmButton.active = !this.nameField.getValue().isEmpty();
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.children.add(nameField);
    }

    public abstract ITextComponent defaultNameFieldString();

    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
        this.renderDirtBackground(0);
        this.nameField.render(matrixStack, x, y, p_230430_4_);
        ITextComponent info = new TranslationTextComponent("Your structure package is not necessarily saved under the same name as it is displayed under");
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
                    this.width / 2, this.height / 2 - 16, 0x999999);// TRANSLATION
            drawCenteredString(matrixStack, font,
                    sb1.toString(),
                    this.width / 2, this.height / 2 - 4, 0x999999);// TRANSLATION
        } else {
            drawCenteredString(matrixStack, font,
                    info.getString(),
                    this.width / 2, this.height / 2 - 16, 0x999999);// TRANSLATION
        }
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
        super.render(matrixStack, x, y, p_230430_4_);
    }

    public abstract void done();
}