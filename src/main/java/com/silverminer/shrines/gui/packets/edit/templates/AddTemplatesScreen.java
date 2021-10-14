package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.TemplateIdentifier;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSAddTemplatesPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class AddTemplatesScreen extends Screen {
    protected final Screen lastScreen;
    protected final String[] files;
    private final List<String> invalidFiles;
    protected AddTemplatesList addTemplatesList;
    protected StructuresPacket packet;
    protected Button saveButton;
    protected Button infoButton;

    protected AddTemplatesScreen(Screen lastScreen, StructuresPacket packet, String[] files) {
        super(new TranslationTextComponent(""));// TRANSLATION
        this.lastScreen = lastScreen;
        this.packet = packet;
        this.files = files;
        this.invalidFiles = Arrays.stream(this.files).filter(s -> !s.endsWith(".nbt")).collect(Collectors.toList());
    }

    public void onClose() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.setScreen(this.lastScreen);
    }

    protected void init() {
        super.init();
        this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
        this.saveButton = this.addButton(new Button(this.width - 60 - 2, 2, 60, 20, new TranslationTextComponent("Save"), (button) -> this.save()));// TRANSLATION
        this.infoButton = this.addButton(new Button(this.width - 60 - 2 - 60 - 2, 2, 60, 20, new TranslationTextComponent("information"), (button) -> {
        }, (button, ms, x, y) -> {
            StringBuilder sb = new StringBuilder();
            for (String s : this.invalidFiles) {
                sb.append(s);
                sb.append("\n");
            }
            StringTextComponent invalidFiles = new StringTextComponent(sb.toString());
            ITextComponent head = new TranslationTextComponent("Left out these files because they don't have the correct extension (.nbt)", invalidFiles);
            this.renderTooltip(ms, head, x, y);
        }));
        this.infoButton.visible = this.invalidFiles.size() > 0;
        this.addTemplatesList = new AddTemplatesList(this.minecraft, this.width, this.height, 26, this.height, 36, this.packet, this, this.files);
        this.children.add(this.addTemplatesList);
    }

    public void save() {
        if (this.minecraft != null && this.minecraft.player != null) {
            List<TemplateIdentifier> templates = this.addTemplatesList.children().stream().map((entry) -> {
                try {
                    return new TemplateIdentifier(new File(entry.getPath()), entry.getLocation());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            int packetId = this.packet.getTempID();
            ShrinesPacketHandler.sendToServer(new CTSAddTemplatesPacket(templates, packetId));

            this.minecraft.setScreen(new WorkingScreen());
        }
    }

    public void toggleSave(boolean active) {
        this.saveButton.active = active;
    }

    @ParametersAreNonnullByDefault
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.addTemplatesList.render(ms, mouseX, mouseY, p_230430_4_);
        drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}