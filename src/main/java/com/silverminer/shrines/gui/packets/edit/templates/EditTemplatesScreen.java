/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class EditTemplatesScreen extends EditStructurePacketScreen {
    protected TemplatesList templatesList;

    public EditTemplatesScreen(Screen lastScreen, StructuresPackageWrapper packet) {
        super(lastScreen, packet);
    }

    protected void init() {
        super.init();
        this.configure.setMessage(new TranslatableComponent("gui.shrines.rename"));
        this.templatesList = new TemplatesList(minecraft, this.width, this.height, this.headerheight, this.bottomheight, 16, () -> this.searchBox.getValue(), packet, this);
        this.addWidget(this.templatesList);
        this.updateButtonStatus();
    }

    @Override
    protected void refreshList(String s) {
        this.templatesList.refreshList(() -> s);
    }

    @Override
    protected void add() {
        if (this.minecraft == null) {
            return;
        }
        TranslatableComponent title = new TranslatableComponent("gui.shrines.templates.add.select");
        String s = TinyFileDialogs.tinyfd_openFileDialog(title.getString(), null, null, null, true);
        if (s == null) {
            return;
        }
        this.minecraft.setScreen(new AddTemplatesScreen(this, this.packet, s.split("\\|")));
    }

    @Override
    protected void delete() {
        if (this.minecraft != null && this.minecraft.player != null) {
            Optional<TemplatesList.Entry> opt = this.templatesList.getSelectedOpt();
            opt.ifPresent(entry -> this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
                if (confirmed) {
                    this.packet.getTemplates().remove(entry.getTemplate().getTemplateID());
                    this.minecraft.setScreen(this.lastScreen);
                }

                this.minecraft.setScreen(this);
            }, new TranslatableComponent("gui.shrines.removeQuestion", entry.getTemplate()),
                    new TranslatableComponent("gui.shrines.removeWarning"),
                    new TranslatableComponent("gui.shrines.delete"), CommonComponents.GUI_CANCEL)));

        }
    }

    @Override
    protected void renameOrConfigure() {
        if (this.minecraft != null) {
            Optional<TemplatesList.Entry> opt = this.templatesList.getSelectedOpt();
            opt.ifPresent(entry -> this.minecraft.setScreen(new RenameTemplateScreen(this, entry.getTemplate(), this.packet)));
        }
    }

    public void updateButtonStatus() {
        this.structuresButton.active = true;
        this.templatesButton.active = false;
        this.poolsButton.active = true;
        this.updateButtonStatus(this.templatesList.getSelectedOpt().isPresent());
        this.configure.active = false;
    }

    @ParametersAreNonnullByDefault
    public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.templatesList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}
