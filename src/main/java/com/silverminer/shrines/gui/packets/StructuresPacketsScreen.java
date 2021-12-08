package com.silverminer.shrines.gui.packets;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.SetNameScreen;
import com.silverminer.shrines.gui.packets.edit.pools.EditPoolsScreen;
import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresScreen;
import com.silverminer.shrines.gui.packets.edit.templates.EditTemplatesScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.LegacyPacketImportUtils;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class StructuresPacketsScreen extends Screen {
    protected static final Logger LOGGER = LogManager.getLogger(StructuresPacketsScreen.class);
    protected final Screen lastScreen;
    private final ArrayList<StructuresPacket> packets;
    protected EditBox searchBox;
    protected StructurePacketsList list;
    protected Button delete;
    protected Button configure;
    protected Button add;
    protected Button rename;
    protected Button export;

    public StructuresPacketsScreen(Screen lastScreen, ArrayList<StructuresPacket> packets) {
        super(new TranslatableComponent("gui.shrines.packets"));
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

    public void openPacketAt(String packetID, int editLocation) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        this.list.refreshList(() -> this.searchBox.getValue());
        List<StructuresPacket> packets = this.list.children().stream().map(StructurePacketsList.Entry::getPacket).filter(pkt -> pkt.getSaveName().equals(packetID)).collect(Collectors.toList());
        if (packets.size() > 0) {
            StructuresPacket packet = packets.get(0);
            switch (editLocation) {
                case 1:
                    this.minecraft.setScreen(new EditTemplatesScreen(packet));
                    break;
                case 2:
                    this.minecraft.setScreen(new EditPoolsScreen(packet));
                    break;
                default:
                    this.minecraft.setScreen(new EditStructuresScreen(packet));
            }
        }
    }

    protected void init() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
        this.searchBox = new EditBox(this.font, (this.width / 4) * 3 - 25, 3, 100, 20, this.searchBox,
                new TextComponent(""));
        this.searchBox.setResponder((string) -> this.list.refreshList(() -> string));
        this.list = new StructurePacketsList(this, this.minecraft, this.width, this.height, 26, this.height - 48, 36,
                () -> this.searchBox.getValue(), this.packets);

        this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));

        this.delete = this.addRenderableWidget(new Button(this.width / 2 - 80 - 80 - 9, this.height - 45, 80, 20,
                new TranslatableComponent("gui.shrines.delete"), (button) -> this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::remove)));

        this.configure = this.addRenderableWidget(new Button(this.width / 2 - 80 - 3, this.height - 45, 80, 20,
                new TranslatableComponent("gui.shrines.configure"), (button) -> this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::configure)));

        this.add = this.addRenderableWidget(new Button(this.width / 2 + 55 + 4, this.height - 22, 110, 20,
                new TranslatableComponent("gui.shrines.add"), (button) -> this.minecraft.setScreen(new SetNameScreen(this,
                new TranslatableComponent("gui.shrines.packets.add.enter_name"),
                TextComponent.EMPTY,
                new TranslatableComponent("gui.shrines.packets.add.info"),
                (value) -> {
                    StructuresPacket packet = new StructuresPacket(value, null, Lists.newArrayList(),
                            false, this.minecraft.player.getName().getString());
                    this.minecraft.setScreen(new ProgressScreen(true));
                    ShrinesPacketHandler
                            .sendToServer(new CTSAddedStructurePacketPacket(packet));
                }))));

        this.rename = this.addRenderableWidget(new Button(this.width / 2 + 3, this.height - 45, 80, 20,
                new TranslatableComponent("gui.shrines.rename"), (button) -> this.list.getSelectedOpt().ifPresent(entry -> this.minecraft
                .setScreen(new SetNameScreen(this,
                        new TranslatableComponent("gui.shrines.packets.rename.enter_name"),
                        new TranslatableComponent(entry.getPacket().getDisplayName()),
                        new TranslatableComponent("gui.shrines.packets.rename.info"),
                        (value) -> {
                            StructuresPacket newPacket = entry.getPacket();
                            newPacket.setDisplayName(value);
                            this.minecraft.setScreen(new ProgressScreen(true));
                            ShrinesPacketHandler
                                    .sendToServer(new CTSEditedStructurePacketPacket(newPacket));
                        })))));

        this.addRenderableWidget(new Button((this.width / 4) * 3 + 79, 3, 40, 20, new TranslatableComponent("gui.shrines.help"), (button) -> this.handleComponentClicked(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Silverminer007/Shrines/wiki")))));
        this.addRenderableWidget(new Button(this.width / 2 - 55, this.height - 22, 110, 20, new TranslatableComponent("gui.shrines.import"), (button) -> this.importUpToDatePacket()));
        this.addRenderableWidget(new Button(this.width / 2 - 55 - 4 - 110, this.height - 22, 110, 20, new TranslatableComponent("gui.shrines.import.legacy"), (button) -> this.importLegacyPacket()));
        this.export = this.addRenderableWidget(new Button(this.width / 2 + 80 + 9, this.height - 45, 80, 20, new TranslatableComponent("gui.shrines.export"), (button) -> this.exportPacket()));
        this.updateButtonStatus(false);
        this.addWidget(this.searchBox);
        this.addWidget(this.list);
        this.setInitialFocus(this.searchBox);
    }

    @ParametersAreNonnullByDefault
    public void render(PoseStack matrixStack, int x, int y, float p_230430_4_) {
        this.list.render(matrixStack, x, y, p_230430_4_);
        this.searchBox.render(matrixStack, x, y, p_230430_4_);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(matrixStack, x, y, p_230430_4_);
    }

    public void updateButtonStatus(boolean hasSelected) {
        this.delete.active = hasSelected;
        this.configure.active = hasSelected;
        this.rename.active = hasSelected;
        this.export.active = hasSelected;
    }

    public void importUpToDatePacket() {
        if (this.minecraft == null) {
            return;
        }
        TranslatableComponent title = new TranslatableComponent("gui.shrines.import.select");
        String s = TinyFileDialogs.tinyfd_openFileDialog(title.getString(), null, null, null, false);
        if (s == null) {
            return;
        }
        try {
            byte[] archive = Files.readAllBytes(Paths.get(s));
            ShrinesPacketHandler.sendToServer(new CTSImportStructuresPacketPacket(new File(s).getName().replace(".zip", ""), archive));
            this.minecraft.setScreen(new ProgressScreen(true));
        } catch (IOException e) {
            ClientUtils.showErrorToast("Failed to import Structures Packet", e.getMessage());
        }
    }

    public void importLegacyPacket() {
        if (this.minecraft != null && this.minecraft.player != null) {
            TranslatableComponent title = new TranslatableComponent("gui.shrines.import.legacy.select");
            String s = TinyFileDialogs.tinyfd_selectFolderDialog(title.getString(), StructureLoadUtils.getShrinesSavesLocation().toString());
            if(s == null){
                return;
            }
            LegacyPacketImportUtils.importLegacyPacket(Paths.get(s), this.minecraft.player);
        }
    }

    public void exportPacket() {
        if (this.minecraft == null) {
            return;
        }
        TranslatableComponent title = new TranslatableComponent("gui.shrines.export.select");
        String s = TinyFileDialogs.tinyfd_selectFolderDialog(title.getString(), System.getProperty("user.home"));
        if (s == null) {
            return;
        }

        this.list.getSelectedOpt().ifPresent(entry -> ShrinesPacketHandler.sendToServer(new CTSExportStructuresPacketPacket(entry.getPacket().getSaveName(), entry.getPacket().getDisplayName(), s)));
    }
}