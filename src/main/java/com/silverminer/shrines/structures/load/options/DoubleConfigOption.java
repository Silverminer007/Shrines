package com.silverminer.shrines.structures.load.options;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;

public class DoubleConfigOption extends ConfigOption<Double> {

    public DoubleConfigOption(String option, Double value, String... comments) {
        super(option, value, comments);
    }

    public DoubleConfigOption(CompoundTag tag) {
        super(tag.getDouble("Value"), tag);
    }

    @Override
    protected Tag writeValue() {
        return DoubleTag.valueOf(this.getValue());
    }
}