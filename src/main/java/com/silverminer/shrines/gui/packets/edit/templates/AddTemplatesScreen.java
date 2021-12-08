package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.TemplateIdentifier;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSAddTemplatesPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
    protected final List<String> invalidFiles;
    protected AddTemplatesList addTemplatesList;
    protected StructuresPacket packet;
    protected Button saveButton;
    protected Button infoButton;

    protected AddTemplatesScreen(Screen lastScreen, StructuresPacket packet, String[] files) {
        super(new TranslatableComponent("gui.shrines.templates.add"));
        this.lastScreen = lastScreen;
        this.packet = packet;
        this.files = files;
        this.invalidFiles = Arrays.stream(this.files).filter(s -> invalidateTemplate(s)).collect(Collectors.toList());
    }

    /**
     *
     * @param s
     * @return true if the given file doesn't point to a valid template file
     */
    protected static boolean invalidateTemplate(String s) {
        try {
            CompoundTag nbt = StructureLoadUtils.readNBTFile(new File(s));
            return nbt.getInt("DataVersion") <= 0 || !s.endsWith(".nbt");
        } catch (Throwable t) {
            return true;
        }
    }

    public void onClose() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.setScreen(this.lastScreen);
    }

    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
        this.saveButton = this.addRenderableWidget(new Button(this.width - 60 - 2, 2, 60, 20, new TranslatableComponent("gui.shrines.save"), (button) -> this.save()));
        this.infoButton = this.addRenderableWidget(new Button(this.width - 60 - 2 - 20 - 2, 2, 20, 20, new TextComponent("?"), (button) -> {
        }, (button, ms, x, y) -> {
            StringBuilder sb = new StringBuilder();
            for (String s : this.invalidFiles) {
                sb.append(s);
                sb.append("\n");
            }
            TextComponent invalidFiles = new TextComponent(sb.toString());
            Component head = new TranslatableComponent("gui.shrines.templates.add.left_out", invalidFiles);
            this.renderTooltip(ms, head, x, y);
        }));
        this.infoButton.visible = this.invalidFiles.size() > 0;
        this.addTemplatesList = new AddTemplatesList(this.minecraft, this.width, this.height, 26, this.height, 36, this.packet, this, this.files);
        this.addWidget(this.addTemplatesList);
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
            ShrinesPacketHandler.sendToServer(new CTSAddTemplatesPacket(templates, this.packet.getSaveName()));

            this.minecraft.setScreen(new ProgressScreen(true));
        }
    }

    public void toggleSave(boolean active) {
        this.saveButton.active = active;
    }

    @ParametersAreNonnullByDefault
    public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.addTemplatesList.render(ms, mouseX, mouseY, p_230430_4_);
        drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}