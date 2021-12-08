package com.silverminer.shrines.gui.packets.edit.structures;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.IDoubleClickScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

public class SelectPoolScreen extends Screen implements IDoubleClickScreen {
    protected final Screen lastScreen;
    protected final StructuresPacket packet;
    protected final StructureData structure;
    protected final ResourceLocation selectedPool;
    protected Button set;
    protected EditBox searchBox;
    protected SelectPoolList templatesList;

    public SelectPoolScreen(Screen lastScreen, StructuresPacket packet, StructureData structure, ResourceLocation selectedPool) {
        super(new TranslatableComponent("gui.shrines.structures.select_pool"));
        this.lastScreen = lastScreen;
        this.packet = packet;
        this.structure = structure;
        this.selectedPool = selectedPool;
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
        this.searchBox = new EditBox(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
                new TextComponent(""));
        this.searchBox.setResponder(this::refreshList);
        this.templatesList = new SelectPoolList(minecraft, this.width, this.height, 26, this.height - 26, 23,
                () -> this.searchBox.getValue(),
                this.packet, this, selectedPool);
        this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
        this.set = this.addRenderableWidget(new Button(this.width / 2 - 50, this.height - 22, 100, 20,
                new TranslatableComponent("gui.shrines.set"), (button) -> this.set()));
        this.addWidget(this.searchBox);
        this.addWidget(this.templatesList);
        this.setInitialFocus(this.searchBox);
        this.updateButtonStatus(this.templatesList.getSelectedOpt().isPresent());
        this.refreshList(searchBox.getValue());
    }

    private void set() {
        this.templatesList.getSelectedOpt().ifPresent(entry -> {
            this.structure.setStart_pool(entry.getPool().getName().toString());
            this.onClose();
        });
    }

    private void refreshList(String s) {
        this.templatesList.refreshList(() -> s);
    }

    @Override
    public void updateButtonStatus(boolean flag) {
        this.set.active = flag;
    }

    @Override
    public Screen getScreen() {
        return this;
    }

    @ParametersAreNonnullByDefault
    public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.templatesList.render(ms, mouseX, mouseY, p_230430_4_);
        drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }

    @Override
    public void onEntryDoubleClicked() {
        this.set();
    }
}
