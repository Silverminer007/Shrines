package com.silverminer.shrines.gui.packets.edit.structures;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;

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
        this.addWidget(this.structuresList);
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
            this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
                if (confirmed) {
                    this.structuresList.getSelectedOpt().ifPresent(entry -> {
                        this.packet.getStructures().remove(entry.getStructure());
                        this.structuresList.refreshList(() -> this.searchBox.getValue());
                    });
                }
                this.minecraft.setScreen(this);
            }, new TranslatableComponent("gui.shrines.removeQuestion",
                    this.structuresList.getSelected().getStructure().getName()),
                    new TranslatableComponent("gui.shrines.removeWarning"),
                    new TranslatableComponent("gui.shrines.delete"),
                    CommonComponents.GUI_CANCEL));
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
    public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.structuresList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}
