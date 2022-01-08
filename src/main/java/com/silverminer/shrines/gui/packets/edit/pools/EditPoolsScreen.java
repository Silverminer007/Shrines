/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.pools;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.misc.SetNameScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.TemplatePool;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSAddTemplatePoolPacket;
import com.silverminer.shrines.utils.network.cts.CTSDeleteTemplatePoolPacket;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class EditPoolsScreen extends EditStructurePacketScreen {
    protected EditPoolsList poolsList;

    public EditPoolsScreen(StructuresPacket packet) {
        super(packet);
    }

    protected void init() {
        super.init();
        this.poolsList = new EditPoolsList(minecraft, this.width, this.height, this.headerheight,
                this.bottomheight, 23, () -> this.searchBox.getValue(), packet, this);
        this.children.add(poolsList);
        this.updateButtonStatus();
        this.refreshList(searchBox.getValue());
    }

    @Override
    protected void refreshList(String s) {
        this.poolsList.refreshList(() -> s);
    }

    @Override
    protected void add() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(new SetNameScreen(this, new TranslationTextComponent("gui.shrines.pools.enter_name"), StringTextComponent.EMPTY, StringTextComponent.EMPTY, (value) -> {
                this.minecraft.setScreen(new WorkingScreen());
                TemplatePool newPool = new TemplatePool(new ResourceLocation(value), Lists.newArrayList());
                ShrinesPacketHandler.sendToServer(new CTSAddTemplatePoolPacket(newPool, this.packet.getSaveName()));
            }, (value) -> !value.isEmpty() && ResourceLocation.isValidResourceLocation(value) && this.packet.getPools().stream().map(pool -> pool.getName().toString()).noneMatch(pool -> pool.equals(value))));
        }
    }

    @Override
    protected void delete() {
        if (this.minecraft != null && this.poolsList.getSelected() != null) {
            this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
                if (confirmed) {
                    this.poolsList.getSelectedOpt().ifPresent(entry -> {
                        TemplatePool pool = entry.getPool();
                        ShrinesPacketHandler.sendToServer(new CTSDeleteTemplatePoolPacket(pool.getSaveName(), this.packet.getSaveName()));
                    });
                }
                this.minecraft.setScreen(this);
            }, new TranslationTextComponent("gui.shrines.removeQuestion",
                    this.poolsList.getSelected().getPool().getName().toString()),
                    new TranslationTextComponent("gui.shrines.removeWarning"),
                    new TranslationTextComponent("gui.shrines.delete"),
                    DialogTexts.GUI_CANCEL));
        }

    }

    @Override
    protected void renameOrConfigure() {
        this.poolsList.getSelectedOpt().ifPresent(EditPoolsList.Entry::configure);
    }

    public void updateButtonStatus() {
        this.structuresButton.active = true;
        this.templatesButton.active = true;
        this.poolsButton.active = false;
        super.updateButtonStatus(this.poolsList.getSelectedOpt().isPresent());
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.poolsList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }

    @Override
    public Screen getScreen() {
        return this;
    }
}
