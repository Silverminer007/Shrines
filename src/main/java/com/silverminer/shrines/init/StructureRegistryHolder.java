package com.silverminer.shrines.init;

import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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