package com.silverminer.shrines.gui.packets.edit.structures;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.IDoubleClickScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class SelectPoolScreen extends Screen implements IDoubleClickScreen {
    protected final Screen lastScreen;
    protected final StructuresPacket packet;
    protected final StructureData structure;
    protected final ResourceLocation selectedPool;
    protected Button set;
    protected TextFieldWidget searchBox;
    protected SelectPoolList templatesList;

    public SelectPoolScreen(Screen lastScreen, StructuresPacket packet, StructureData structure, ResourceLocation selectedPool) {
        super(new TranslationTextComponent("gui.shrines.structures.select_pool"));
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
        this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
                new StringTextComponent(""));
        this.searchBox.setResponder(this::refreshList);
        this.templatesList = new SelectPoolList(minecraft, this.width, this.height, 26, this.height - 26, 23,
                () -> this.searchBox.getValue(),
                this.packet, this, selectedPool);
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.set = this.addButton(new Button(this.width / 2 - 50, this.height - 22, 100, 20,
                new TranslationTextComponent("gui.shrines.set"), (button) -> this.set()));
        this.children.add(searchBox);
        this.children.add(this.templatesList);
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
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.templatesList.render(ms, mouseX, mouseY, p_230430_4_);
        drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }

    @Override
    public void onEntryDoubleClicked() {
        this.set();
    }
}
