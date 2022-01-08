/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.io.File;

public class TemplateIdentifier {
    private final CompoundNBT template;
    private final ResourceLocation location;

    public TemplateIdentifier(CompoundNBT nbt, ResourceLocation location) {
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

    public static TemplateIdentifier read(CompoundNBT tag) {
        CompoundNBT template = tag.getCompound("template");
        ResourceLocation location = new ResourceLocation(tag.getString("location"));
        return new TemplateIdentifier(template, location);
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public CompoundNBT getTemplate() {
        return template;
    }

    public CompoundNBT write() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("template", this.template);
        tag.putString("location", this.location.toString());
        return tag;
    }
}
