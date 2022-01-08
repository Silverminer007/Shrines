/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.misc;

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
import java.util.function.Consumer;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class SetNameScreen extends Screen {
    protected final Screen lastScreen;
    private final ITextComponent defaultText;
    private final ITextComponent info;
    private final Consumer<String> done;
    private final Function<String, Boolean> validator;
    protected TextFieldWidget nameField;
    protected Button confirmButton;

    public SetNameScreen(Screen lastScreen, ITextComponent title, ITextComponent defaultText, ITextComponent info, Consumer<String> done) {
        this(lastScreen, title, defaultText, info, done, (value) -> !value.isEmpty());
    }

    public SetNameScreen(Screen lastScreen, ITextComponent title, ITextComponent defaultText, ITextComponent info, Consumer<String> done, Function<String, Boolean> validator) {
        super(title);
        this.lastScreen = lastScreen;
        this.defaultText = defaultText;
        this.info = info;
        this.done = done;
        this.validator = validator;
    }

    public void onClose() {
        this.minecraft.setScreen(lastScreen);
    }

    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
        String message = this.nameField == null ? this.defaultText.getString() : this.nameField.getValue();
        this.nameField = new TextFieldWidget(font, this.width / 2 - 75, this.height / 2 - 40, 150, 20,
                StringTextComponent.EMPTY);
        this.nameField.setValue(message);
        this.nameField.setResponder(s -> this.confirmButton.active = validator.apply(s));
        this.confirmButton = this.addButton(new Button(this.width / 2 - 70, this.height / 2 + 20, 140, 20,
                new TranslationTextComponent("gui.shrines.confirm"), (button) -> this.done.accept(this.nameField.getValue())));
        this.confirmButton.active = validator.apply(this.nameField.getValue());
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.children.add(nameField);
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
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