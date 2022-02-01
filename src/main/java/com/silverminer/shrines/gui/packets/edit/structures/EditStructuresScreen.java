/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.structures;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class EditStructuresScreen extends EditStructurePacketScreen {
    protected EditStructuresList structuresList;

    public EditStructuresScreen(StructuresPacket packet) {
        super(packet);
    }

    protected void init() {
        super.init();
        this.structuresList = new EditStructuresList(minecraft, this.width, this.height, this.headerheight,
                this.bottomheight, 35, () -> this.searchBox.getValue(), packet, this);
        this.children.add(structuresList);
        this.updateButtonStatus();
    }

    @Override
    protected void refreshList(String s) {
        this.structuresList.refreshList(() -> s);
    }

    @Override
    protected void add() {
        this.packet.getStructures().add(new StructureData(DefaultStructureConfig.CUSTOM));
        this.structuresList.refreshList(() -> this.searchBox.getValue());
        this.structuresList.setSelected(this.structuresList.children().get(0));
        this.structuresList.getSelectedOpt().ifPresent(EditStructuresList.Entry::configure);
    }

    @Override
    protected void delete() {
        if (this.minecraft != null && this.structuresList.getSelected() != null) {
            if(this.structuresList.getSelected().getStructure().getName().equals("___ Deleted Structure ___")) {
                this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
                    if (confirmed) {
                        this.structuresList.getSelectedOpt().ifPresent(entry -> {
                            this.packet.getStructures().remove(entry.getStructure());
                            this.structuresList.refreshList(() -> this.searchBox.getValue());
                        });
                    }
                    this.minecraft.setScreen(this);
                }, new TranslationTextComponent("gui.shrines.removeQuestion",
                      this.structuresList.getSelected().getStructure().getName()),
                      new TranslationTextComponent("gui.shrines.removeWarning"),
                      new TranslationTextComponent("gui.shrines.delete"),
                      DialogTexts.GUI_CANCEL));
            } else {
                StructureData deletedStructure = new StructureData(DefaultStructureConfig.DELETED_STRUCTURE_CONFIG);
                deletedStructure.setKey(this.structuresList.getSelected().getStructure().getKey());
                this.packet.getStructures().remove(this.structuresList.getSelected().getStructure());
                this.packet.getStructures().add(deletedStructure);
                this.structuresList.refreshList(() -> this.searchBox.getValue());
            }
        }

    }

    @Override
    protected void renameOrConfigure() {
        this.structuresList.getSelectedOpt().ifPresent(EditStructuresList.Entry::configure);
    }

    public void updateButtonStatus() {
        this.structuresButton.active = false;
        this.templatesButton.active = true;
        this.poolsButton.active = true;
        super.updateButtonStatus(this.structuresList.getSelectedOpt().isPresent());
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.structuresList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}
