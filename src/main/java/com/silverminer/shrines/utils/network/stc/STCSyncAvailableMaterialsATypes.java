/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class STCSyncAvailableMaterialsATypes implements IPacket {
    private final List<String> availableMaterials;
    private final List<String> availableTypes;

    public STCSyncAvailableMaterialsATypes(List<String> availableDimensions, List<String> availableTypes) {
        this.availableMaterials = availableDimensions;
        this.availableTypes = availableTypes;
    }

    public STCSyncAvailableMaterialsATypes(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        if (tag == null) {
            this.availableMaterials = new ArrayList<>();
        } else {
            ListTag dimensions = tag.getList("materials", 8);// Type 8 is StringTag
            this.availableMaterials = dimensions.stream().filter(dimension -> dimension instanceof StringTag).map(Tag::getAsString).collect(Collectors.toList());
        }
        if (tag == null) {
            this.availableTypes = new ArrayList<>();
        } else {
            ListTag dimensions = tag.getList("types", 8);// Type 8 is StringTag
            this.availableTypes = dimensions.stream().filter(dimension -> dimension instanceof StringTag).map(Tag::getAsString).collect(Collectors.toList());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        ListTag materials = new ListTag();
        materials.addAll(this.availableMaterials.stream().map(StringTag::valueOf).toList());
        tag.put("materials", materials);
        ListTag types = new ListTag();
        types.addAll(this.availableTypes.stream().map(StringTag::valueOf).toList());
        tag.put("types", types);
        buf.writeNbt(tag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PackageManagerProvider.CLIENT.setAvailableMaterials(this.availableMaterials);
            PackageManagerProvider.CLIENT.setAvailableTypes(this.availableTypes);
        });
        ctx.get().setPacketHandled(true);
    }
}