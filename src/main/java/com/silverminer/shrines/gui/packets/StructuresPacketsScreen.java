package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresScreen;
import com.silverminer.shrines.gui.packets.edit.templates.EditTemplatesScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSPlayerLeftQueuePacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class StructuresPacketsScreen extends Screen {
    protected final Screen lastScreen;
    private final ArrayList<StructuresPacket> packets;
    protected TextFieldWidget searchBox;
    protected StructurePacketsList list;
    protected Button delete;
    protected Button configure;
    protected Button add;
    protected Button rename;

    public StructuresPacketsScreen(Screen lastScreen, ArrayList<StructuresPacket> packets) {
        super(new TranslationTextComponent("Structure Packets"));// TRANSLATION
        this.lastScreen = lastScreen;
        this.packets = packets;
    }

    public void onClose() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        this.minecraft.setScreen(this.lastScreen);
        ShrinesPacketHandler.sendToServer(new CTSPlayerLeftQueuePacket());
    }

    public void openPacketAt(int packetID, int editLocation) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        this.list.refreshList(() -> this.searchBox.getValue());
        List<StructuresPacket> packets = this.list.children().stream().map(StructurePacketsList.Entry::getPacket).filter(pkt -> pkt.getTempID() == packetID).collect(Collectors.toList());
        if (packets.size() > 0) {
            StructuresPacket packet = packets.get(0);
            switch (editLocation) {
                case 0:
                    this.minecraft.setScreen(new EditStructuresScreen(packet));
                    break;
                case 1:
                    this.minecraft.setScreen(new EditTemplatesScreen(packet));
                    break;
                default:
                    // TODO Add Pools Screen and open it here
            }
        }
    }

    protected void init() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
        this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
                new StringTextComponent(""));
        this.searchBox.setResponder((string) -> this.list.refreshList(() -> string));
        this.list = new StructurePacketsList(this, this.minecraft, this.width, this.height, 26, this.height - 26, 36,
                () -> this.searchBox.getValue(), this.packets);
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.delete = this.addButton(new Button(this.width / 2 - 80 - 80 - 9, this.height - 22, 80, 20,
                new TranslationTextComponent("Delete"), (button) -> this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::remove)));// TRANSLATION
        this.configure = this.addButton(new Button(this.width / 2 - 80 - 3, this.height - 22, 80, 20,
                new TranslationTextComponent("Configure"), (button) -> this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::configure)));// TRANSLATION
        this.add = this.addButton(new Button(this.width / 2 + 3, this.height - 22, 80, 20,
                new TranslationTextComponent("Add"), (button) -> this.minecraft.setScreen(new AddStructurePacketScreen(this))));// TRANSLATION
        this.rename = this.addButton(new Button(this.width / 2 + 80 + 9, this.height - 22, 80, 20,
                new TranslationTextComponent("Rename"), (button) -> this.list.getSelectedOpt().ifPresent(entry -> this.minecraft
                .setScreen(new RenameStructurePacketScreen(this, entry.getPacket())))));// TRANSLATION
        this.updateButtonStatus(false);
        this.children.add(this.searchBox);
        this.children.add(this.list);
        this.setInitialFocus(this.searchBox);
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
        this.list.render(matrixStack, x, y, p_230430_4_);
        this.searchBox.render(matrixStack, x, y, p_230430_4_);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(matrixStack, x, y, p_230430_4_);
    }

    public void updateButtonStatus(boolean hasSelected) {
        this.delete.active = hasSelected;
        this.configure.active = hasSelected;
        this.rename.active = hasSelected;
    }
}