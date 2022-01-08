/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSRenameTemplatesPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class RenameTemplateScreen extends Screen {
    protected final Screen lastScreen;
    protected final ResourceLocation template;
    protected final StructuresPacket packet;
    protected ITextComponent info;
    protected TextFieldWidget nameField;
    protected Button confirmButton;

    protected RenameTemplateScreen(Screen lastScreen, ResourceLocation template, StructuresPacket packet) {
        super(new TranslationTextComponent("gui.shrines.packets.add"));
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
        this.nameField = new TextFieldWidget(font, this.width / 2 - 75, this.height / 2 - 40, 150, 20,
                StringTextComponent.EMPTY);
        this.nameField.setValue(message);
        this.nameField.setResponder(this::updateSaveButton);
        this.confirmButton = this.addButton(new Button(this.width / 2 - 70, this.height / 2 + 20, 140, 20,
                new TranslationTextComponent("gui.shrines.confirm"), (button) -> this.done()));
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.children.add(nameField);
        this.updateSaveButton(this.nameField.getValue());
    }

    private void updateSaveButton(String newName) {
        try {
            // That expression looks a bit strange at first, but I strip the extension .nbt, and then I add it again to prevent double extensions if there was already one
            this.confirmButton.active = packet.getTemplates().stream().map(Object::toString).map(temp -> temp.replace(".nbt", "")).noneMatch(temp -> temp.equals(new ResourceLocation(newName).toString().replace(".nbt", "")));
            if (this.confirmButton.active) {
                this.info = StringTextComponent.EMPTY;
            } else {
                this.info = new TranslationTextComponent("gui.shrines.templates.rename.name_taken");
            }
        } catch (ResourceLocationException e) {
            this.confirmButton.active = false;
            this.info = new TranslationTextComponent("gui.shrines.templates.rename.invalid_name");
        }
    }

    private void done() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(new WorkingScreen());
        }
        if (this.minecraft.player != null) {
            ShrinesPacketHandler
                    .sendToServer(new CTSRenameTemplatesPacket(this.template, new ResourceLocation(this.nameField.getValue()), this.packet.getSaveName()));
        }
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
        this.renderDirtBackground(0);
        this.nameField.render(matrixStack, x, y, p_230430_4_);
        drawCenteredString(matrixStack, this.font, new TranslationTextComponent("gui.shrines.templates.rename.old_name", this.template.toString()), this.width / 2, this.height / 2 - 52, 0xcdcdcd);
        drawCenteredString(matrixStack, this.font, this.info, this.width / 2, this.height / 2 - 10, 0xaa0000);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
        super.render(matrixStack, x, y, p_230430_4_);
    }
}