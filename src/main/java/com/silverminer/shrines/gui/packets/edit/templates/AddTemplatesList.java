package com.silverminer.shrines.gui.packets.edit.templates;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddTemplatesList extends ObjectSelectionList<AddTemplatesList.Entry> {
    protected static final Logger LOGGER = LogManager.getLogger(AddTemplatesList.class);
    private final AddTemplatesScreen screen;
    private final StructuresPackageWrapper packet;
    private final String[] files;


    public AddTemplatesList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_, StructuresPackageWrapper packet, AddTemplatesScreen screen, String[] files) {
        super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
        this.packet = packet;
        this.screen = screen;
        this.files = files;
        this.refreshList();
    }

    public void refreshList() {
        this.clearEntries();
        for (String path : files) {
            if (!screen.invalidFiles.contains(path))
                this.addEntry(new Entry(path));
        }
        AddTemplatesList.this.updateTemplateDuplicate();
    }

    private boolean hasTemplate(ResourceLocation template) {
        if (packet.getTemplates().getAsStream().map(Object::toString).map(temp -> temp.replace(".nbt", "")).anyMatch(temp -> temp.equals(template.toString().replace(".nbt", "")))) {
            // This expression looks a bit strange at first glance, but I strip the extension .nbt stand and then add it back to avoid duplicate extensions if one was already there
            return true;
        }
        return this.children().stream().map(AddTemplatesList.Entry::getLocation).filter(loc -> loc.toString().equals(template.toString())).count() > 1;
    }

    private void updateTemplateDuplicate() {
        // That expression looks a bit strange at first, but I strip the extension .nbt, and then I add it again to prevent double extensions if there was already one
        // That line is not clean so here is an explanation:
        // I want to set the Save Button to active or inactive.
        // To check if it should be active I read all Templates (new first and old later) and convert their ResourceLocation in Strings
        List<String> templates = this.children().stream().map(AddTemplatesList.Entry::getLocation).filter(Objects::nonNull).map(Object::toString).map(loc -> loc.replace(".nbt", "")).collect(Collectors.toList());
        // First parts checks if anything was filtered in the first step. The only reason for that could be a null element which indicates an error
        // Second part checks if there are duplicates. Sets don't allow duplicated elements so any duplicate is going to be removed and the size will be smaller
        boolean hasErrors = templates.size() < this.children().size() || templates.stream().distinct().count() < this.children().size();
        // Now add all old templates and check if there is a duplicate
        templates.addAll(this.packet.getTemplates().getAsStream().map(Object::toString).map(loc -> loc.replace(".nbt", "")).toList());
        hasErrors = hasErrors || templates.stream().distinct().count() < templates.size();
        this.screen.toggleSave(!hasErrors);
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 60;
    }

    public int getRowWidth() {
        return this.width - 10;
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    public Optional<AddTemplatesList.Entry> getSelectedOpt() {
        return Optional.ofNullable(this.getSelected());
    }

    public final class Entry extends ObjectSelectionList.Entry<AddTemplatesList.Entry> implements ContainerEventHandler {
        private final EditBox textField;
        private final Minecraft minecraft;
        private final String path;
        private final ArrayList<GuiEventListener> children = Lists.newArrayList();
        private ResourceLocation location;
        @Nullable
        private GuiEventListener focused;
        private boolean dragging;

        public Entry(String path) {
            this.path = path;
            this.minecraft = Minecraft.getInstance();
            this.textField = new EditBox(this.minecraft.font, 0, 0, 200, 20, TextComponent.EMPTY);
            this.textField.setMaxLength(256);
            this.textField.setResponder(text -> {
                try {
                    this.location = new ResourceLocation(text.replace(".nbt", ""));
                    if (!AddTemplatesList.this.hasTemplate(this.location)) {
                        this.textField.setTextColor(0xffffff);
                    } else {
                        this.textField.setTextColor(0xff0000);
                    }
                    AddTemplatesList.this.updateTemplateDuplicate();
                } catch (Throwable t) {
                    this.textField.setTextColor(0xff0000);
                    this.location = null;
                    AddTemplatesList.this.screen.toggleSave(false);
                }
            });
            this.textField.setValue(new ResourceLocation(ShrinesMod.MODID, new File(path).getName()).toString().replace(".nbt", ""));
            this.children.add(textField);
        }

        @ParametersAreNonnullByDefault
        @Override
        public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            this.textField.x = left + 2;
            this.textField.y = top + this.minecraft.font.lineHeight + 4;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
            this.minecraft.font.draw(ms, path, left + 2, top + 1, 0x999999);
        }

        @Nonnull
        @Override
        public List<? extends GuiEventListener> children() {
            return children;
        }

        public boolean isDragging() {
            return this.dragging;
        }

        public void setDragging(boolean p_231037_1_) {
            this.dragging = p_231037_1_;
        }

        @Nullable
        public GuiEventListener getFocused() {
            return this.focused;
        }

        public void setFocused(@Nullable GuiEventListener p_231035_1_) {
            this.focused = p_231035_1_;
        }

        public String getPath() {
            return path;
        }

        public ResourceLocation getLocation() {
            return location;
        }

        @Override
        public @NotNull Component getNarration() {
            return new TextComponent(this.getPath());
        }
    }
}
