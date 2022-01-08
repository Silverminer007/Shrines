/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.IDoubleClickScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Collectors;

public class AddTemplateToPoolScreen extends Screen implements IDoubleClickScreen {
    protected final Screen lastScreen;
    protected final StructuresPacket packet;
    protected final TemplatePool templatePool;
    protected Button add;
    protected TextFieldWidget searchBox;
    protected SelectTemplatesList templatesList;

    public AddTemplateToPoolScreen(Screen lastScreen, StructuresPacket packet, TemplatePool templatePool) {
        super(new TranslationTextComponent("gui.shrines.pools.select_templates"));
        this.lastScreen = lastScreen;
        this.packet = packet;
        this.templatePool = templatePool;
    }

    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.lastScreen);
        }
    }

    protected void init() {
        if (this.minecraft == null) {
            return;
        }
        this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
                StringTextComponent.EMPTY);
        this.searchBox.setResponder(this::refreshList);
        this.templatesList = new SelectTemplatesList(minecraft, this.width, this.height, 26, this.height - 26, 16,
                () -> this.searchBox.getValue(),
                packet, this, this.templatePool.getEntries().stream().map(TemplatePool.Entry::getTemplate).collect(Collectors.toList()));
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.add = this.addButton(new Button(this.width / 2 - 50, this.height - 22, 100, 20,
                new TranslationTextComponent("gui.shrines.add"), (button) -> this.add()));
        this.children.add(searchBox);
        this.children.add(this.templatesList);
        this.setInitialFocus(this.searchBox);
        this.updateButtonStatus(this.templatesList.getSelectedOpt().isPresent());
    }

    private void add() {
        for(SelectTemplatesList.MultipleSelectEntry entry : this.templatesList.getSelectEntries()){
            this.templatePool.getEntries().add(new TemplatePool.Entry(entry.getTemplate()));
        }
        this.onClose();
    }

    private void refreshList(String s) {
        this.templatesList.refreshList(() -> s);
    }

    @Override
    public void updateButtonStatus(boolean flag) {
        this.add.active = flag;
    }

    @Override
    public Screen getScreen() {
        return this;
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.templatesList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }

    @Override
    public void onEntryDoubleClicked() {
        this.add();
    }
}
