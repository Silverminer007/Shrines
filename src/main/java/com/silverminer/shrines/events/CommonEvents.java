/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.events;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.init.StructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.NewVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.NovelsData;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructureNovel;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.packages.server.NovelsDataRegistry;
import com.silverminer.shrines.utils.StructureRegistrationUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.worldgen.processors.ProcessorTypes;
import com.silverminer.shrines.worldgen.structures.ShrinesStructure;
import com.silverminer.shrines.worldgen.structures.variation.NewVariationMaterial;
import com.silverminer.shrines.worldgen.structures.variation.RandomVariationProcessable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.WritableRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonEvents {
    protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

    @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD)
    public static class ModEventBus {

        @SubscribeEvent
        public static void commonSetupEvent(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                ShrinesPacketHandler.register();
                ProcessorTypes.register();
                StructureRegistrationUtils.setupWorldGen();
                PackageManagerProvider.SERVER.loadPackages();
            });
        }

        @SubscribeEvent
        public static void addPackFinder(AddPackFindersEvent event) {
            // Packages are registered here. Icon Cache is registered in Client Events, because we don't need it on servers
            PackSource source = PackSource.decorating("pack.source.shrines");
            event.addRepositorySource(new FolderRepositorySource(DirectoryStructureAccessor.RECENT.getPackagesPath().toFile(), source));
        }
    }

    @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE)
    public static class ForgeEventBus {

        @SubscribeEvent
        public static void onServerAboutToStartEvent(ServerAboutToStartEvent event) {
            if (ShrinesMod.debug) {
                WritableRegistry<NewVariationMaterial> variationMaterialRegistry = event.getServer().registryAccess().ownedRegistryOrThrow(NewVariationMaterial.REGISTRY);
                for (var entry : variationMaterialRegistry.entrySet()) {
                    LOGGER.info("NewVariationMaterialRegistry. Element key: {}, Value: {}", entry.getKey(), entry.getValue());
                }
                WritableRegistry<StructureNovel> structureNovelsRegistry = event.getServer().registryAccess().ownedRegistryOrThrow(StructureNovel.REGISTRY);
                for (var entry : structureNovelsRegistry.entrySet()) {
                    LOGGER.info("StructureNovel. Element key: {}, Value: {}", entry.getKey(), entry.getValue());
                }
                WritableRegistry<NewVariationConfiguration> variationConfigurationRegistry = event.getServer().registryAccess().ownedRegistryOrThrow(NewVariationConfiguration.REGISTRY);
                for (var entry : variationConfigurationRegistry.entrySet()) {
                    LOGGER.info("VariationConfiguration. Element key: {}, Value: {}", entry.getKey(), entry.getValue());
                }
            }
        }

        @SubscribeEvent
        public static void onWorldLoad(WorldEvent.Load event) {
            if (event.getWorld() instanceof ServerLevel serverLevel) {
                StructureRegistrationUtils.addDimensionalSpacing(serverLevel);
                if (!serverLevel.isClientSide() && serverLevel.dimension() == Level.OVERWORLD) {
                    NovelsDataRegistry.loadData(serverLevel);
                }
            }
        }

        @SubscribeEvent
        public static void onWorldSave(WorldEvent.Save event) {
            // Very hacky reset of the random variation remaps at every world load
            for (StructureFeature<?> structureFeature : ForgeRegistries.STRUCTURE_FEATURES) {
                ((RandomVariationProcessable) structureFeature).getRandomVariationProcessor().resetRemaps();
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent event) {
            if (event.phase == Phase.END) {
                if (event.player instanceof ServerPlayer serverPlayer) {
                    if (event.player.tickCount % 50 == 0) {
                        NovelsDataRegistry.INSTANCE.setDirty();
                        BlockPos playerPos = serverPlayer.blockPosition();
                        for (StructureRegistryHolder holder : StructureInit.STRUCTURES) {
                            ShrinesStructure structure = holder.getStructure();
                            ServerLevel world = serverPlayer.getLevel();
                            if (world.structureFeatureManager().getStructureAt(playerPos, structure).isValid()) {
                                StructureData data = structure.getConfig();
                                NovelsData novel = null;
                                if (NovelsDataRegistry.INSTANCE.hasNovelOf(serverPlayer.getUUID(), data.getKey().toString())) {
                                    novel = NovelsDataRegistry.INSTANCE.getNovelOf(serverPlayer.getUUID(), data.getKey().toString());
                                }
                                if (novel == null) {
                                    novel = new NovelsData(data.getKey().toString());
                                }
                                boolean isNew = true;
                                for (BlockPos pos : novel.getFoundStructures()) {
                                    if (pos.closerThan(playerPos, 100)) {
                                        isNew = false;
                                        break;
                                    }
                                }
                                if (!isNew) {
                                    break;
                                }
                                novel.addFoundStructure(playerPos);
                                NovelsDataRegistry.INSTANCE.setNovelOf(serverPlayer.getUUID(), data.getKey().toString(), novel);
                            }
                        }
                    }
                }
            }
        }
    }
}