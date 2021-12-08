package com.silverminer.shrines.init;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;

import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class StructureRegistryHolder {
    protected static final Logger LOGGER = LogManager.getLogger(StructureRegistryHolder.class);
    private final ShrinesStructure structure;
    private ConfiguredStructureFeature<?, ?> configuredStructure;

    public StructureRegistryHolder(String name, StructureData config) {
        this.structure = new ShrinesStructure(name, config);
        this.configure();
    }

    public ShrinesStructure getStructure() {
        return structure;
    }

    public ConfiguredStructureFeature<?, ?> getConfiguredStructure() {
        return configuredStructure;
    }

    private void configure() {
        this.configuredStructure = structure.configured(new JigsawConfiguration(() -> PlainVillagePools.START, 1));
    }
}