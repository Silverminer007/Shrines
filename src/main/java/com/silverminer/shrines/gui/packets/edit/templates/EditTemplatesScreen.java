package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSDeleteTemplatesPacket;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class EditTemplatesScreen extends EditStructurePacketScreen {
    protected TemplatesList templatesList;

    public EditTemplatesScreen(StructuresPacket packet) {
        super(packet);
    }

    protected void init() {
        super.init();
        this.configure.setMessage(new TranslationTextComponent("gui.shrines.rename"));
        this.templatesList = new TemplatesList(minecraft, this.width, this.height, this.headerheight, this.bottomheight, 16, () -> this.searchBox.getValue(), packet, this);
        this.children.add(templatesList);
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
        TranslationTextComponent title = new TranslationTextComponent("gui.shrines.templates.add.select");
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
                    ShrinesPacketHandler.sendToServer(new CTSDeleteTemplatesPacket(new ResourceLocation(entry.getTemplate()), this.packet.getSaveName()));

                    this.minecraft.setScreen(new WorkingScreen());
                }

                this.minecraft.setScreen(this);
            }, new TranslationTextComponent("gui.shrines.removeQuestion", entry.getTemplate()),
                    new TranslationTextComponent("gui.shrines.removeWarning"),
                    new TranslationTextComponent("gui.shrines.delete"), DialogTexts.GUI_CANCEL)));

        }
    }

    @Override
    protected void renameOrConfigure() {
        if (this.minecraft != null) {
            Optional<TemplatesList.Entry> opt = this.templatesList.getSelectedOpt();
            opt.ifPresent(entry -> this.minecraft.setScreen(new RenameTemplateScreen(this, new ResourceLocation(entry.getTemplate()), this.packet)));
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
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.templatesList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}
