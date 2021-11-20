package com.silverminer.shrines.gui.packets.edit;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.IUpdatableScreen;
import com.silverminer.shrines.gui.packets.edit.pools.EditPoolsScreen;
import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresScreen;
import com.silverminer.shrines.gui.packets.edit.templates.EditTemplatesScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSEditedStructurePacketPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class EditStructurePacketScreen extends Screen implements IUpdatableScreen {
    protected final StructuresPacket packet;
    protected TextFieldWidget searchBox;
    protected Button structuresButton;
    protected Button templatesButton;
    protected Button poolsButton;
    protected Button delete;
    protected Button configure;
    protected Button add;
    protected int headerheight;
    protected int bottomheight;
    protected boolean showHeader;

    public EditStructurePacketScreen(StructuresPacket packet) {
        this(new TranslationTextComponent("gui.shrines.edit_packet"), packet, true);
    }

    public EditStructurePacketScreen(ITextComponent title, StructuresPacket packet, boolean showHeader) {
        super(title);
        this.packet = packet;
        this.showHeader = showHeader;
    }

    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(new WorkingScreen());
            ShrinesPacketHandler.sendToServer(
                    new CTSEditedStructurePacketPacket(this.packet));
        }
    }

    protected void init() {
        if (this.minecraft == null) {
            return;
        }
        this.headerheight = 46;
        this.bottomheight = this.height - 26;
        this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
                new StringTextComponent(""));
        this.searchBox.setResponder(this::refreshList);
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        if (this.showHeader) {
            this.structuresButton = this
                    .addButton(new Button(2, 24, 150, 20, new TranslationTextComponent("gui.shrines.structures"), (button) -> this.minecraft.setScreen(new EditStructuresScreen(this.packet))));
            this.templatesButton = this
                    .addButton(new Button(165, 24, 150, 20, new TranslationTextComponent("gui.shrines.templates"), (button) -> this.minecraft.setScreen(new EditTemplatesScreen(this.packet))));
            this.poolsButton = this
                    .addButton(new Button(326, 24, 150, 20, new TranslationTextComponent("gui.shrines.pools"), (button) -> this.minecraft.setScreen(new EditPoolsScreen(this.packet))));
        }
        this.delete = this.addButton(new Button(this.width / 2 - 40 - 80 - 3, this.height - 22, 80, 20,
                new TranslationTextComponent("gui.shrines.delete"), (button) -> this.delete()));
        this.configure = this.addButton(new Button(this.width / 2 - 40, this.height - 22, 80, 20,
                new TranslationTextComponent("gui.shrines.configure"), (button) -> this.renameOrConfigure()));
        this.add = this.addButton(new Button(this.width / 2 + 40 + 3, this.height - 22, 80, 20,
                new TranslationTextComponent("gui.shrines.add"), (button) -> this.add()));
        this.children.add(searchBox);
        this.setInitialFocus(this.searchBox);
    }

    protected abstract void refreshList(String s);

    protected abstract void add();

    protected abstract void delete();

    protected abstract void renameOrConfigure();

    public abstract void updateButtonStatus();

    public void updateButtonStatus(boolean hasSelected) {
        this.delete.active = hasSelected;
        this.configure.active = hasSelected;
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.searchBox.render(ms, mouseX, mouseY, p_230430_4_);
        drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }

    @Override
    public Screen getScreen() {
        return this;
    }
}