package com.silverminer.shrines.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.io.File;

public class TemplateIdentifier {
    private final CompoundTag template;
    private final ResourceLocation location;

    public TemplateIdentifier(CompoundTag nbt, ResourceLocation location) {
        this.template = nbt;
        this.location = location;
    }

    public TemplateIdentifier(File file, ResourceLocation location) {
        this.template = StructureLoadUtils.readNBTFile(file);
        if(this.template == null){
            throw new IllegalArgumentException("Failed to load template from " + file);
        }
        this.location = location;
    }

    public static TemplateIdentifier read(CompoundTag tag) {
        CompoundTag template = tag.getCompound("template");
        ResourceLocation location = new ResourceLocation(tag.getString("location"));
        return new TemplateIdentifier(template, location);
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public CompoundTag getTemplate() {
        return template;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.put("template", this.template);
        tag.putString("location", this.location.toString());
        return tag;
    }
}
