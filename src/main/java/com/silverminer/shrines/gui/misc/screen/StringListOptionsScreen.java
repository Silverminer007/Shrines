/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.misc.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.utils.ClientUtils;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class StringListOptionsScreen extends Screen {
    protected Screen lastScreen;
    protected EditBox searchBox;
    protected StringListOptionsList list;
    protected final List<String> possibleValues;
    protected final ArrayList<String> selectedValues;
    protected final Consumer<List<String>> setter;

    public StringListOptionsScreen(Screen lastScreen, List<String> possibleOptions, ArrayList<String> selectedOptions,
                                   Component title, Consumer<List<String>> setter) {
        super(title);
        this.lastScreen = lastScreen;
        this.possibleValues = possibleOptions;
        this.selectedValues = selectedOptions;
        this.setter = setter;
    }

    public void onClose() {
        this.setter.accept(selectedValues);
        this.minecraft.setScreen(this.lastScreen);
    }

    protected void init() {
        this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
        this.searchBox = new EditBox(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
                new TextComponent(""));
        this.searchBox.setResponder((string) -> this.list.refreshList(() -> string));
        this.list = new StringListOptionsList(this, minecraft, this.width, this.height, 26, this.height, 26,
                () -> this.searchBox.getValue());
        this.addWidget(this.searchBox);
        this.addWidget(this.list);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.searchBox.render(ms, mouseX, mouseY, partialTicks);
        this.list.render(ms, mouseX, mouseY, partialTicks);
        drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(ms, mouseX, mouseY, partialTicks);
    }
}