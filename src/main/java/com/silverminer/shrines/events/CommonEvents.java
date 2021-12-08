/*
 * Silverminer (and Team)
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 *
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.events;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.novels.NovelsData;
import com.silverminer.shrines.structures.novels.NovelsDataRegistry;
import com.silverminer.shrines.structures.processors.ProcessorTypes;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.StructureRegistrationUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCCacheStructureIconsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
            });
        }

        @SubscribeEvent
        public static void addPackFinder(AddPackFindersEvent event){
            for(RepositorySource source : StructureLoadUtils.getPackFinders()){
                event.addRepositorySource(source);
            }
        }
    }

    @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE)
    public static class ForgeEventBus {

        @SubscribeEvent
        public static void onWorldLoad(WorldEvent.Load event) {
            if (event.getWorld() instanceof ServerLevel) {
                StructureRegistrationUtils.addDimensionalSpacing((ServerLevel) event.getWorld());
            }
            LevelAccessor iworld = event.getWorld();

            if (iworld instanceof ServerLevel) {
                ServerLevel world = (ServerLevel) iworld;
                if (!world.isClientSide() && world.dimension() == Level.OVERWORLD) {
                    NovelsDataRegistry.loadData(world);
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent event) {
            if (event.phase == Phase.END) {
                if (event.player instanceof ServerPlayer) {
                    if (event.player.tickCount % 50 == 0) {
                        NovelsDataRegistry.INSTANCE.setDirty();
                        BlockPos playerPos = event.player.blockPosition();
                        for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
                            ShrinesStructure structure = holder.getStructure();
                            ServerLevel world = ((ServerPlayer) event.player).getLevel();
                            if (world.structureFeatureManager().getStructureAt(playerPos, structure).isValid()) {
                                StructureData data = structure.getConfig();
                                NovelsData novel = null;
                                if (NovelsDataRegistry.INSTANCE.hasNovelOf(data.getKey())) {
                                    novel = NovelsDataRegistry.INSTANCE.getNovelOf(data.getKey());
                                }
                                if (novel == null) {
                                    novel = new NovelsData(data.getKey());
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
                                NovelsDataRegistry.INSTANCE.setNovelOf(data.getKey(), novel);
                            }
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            ShrinesPacketHandler.sendTo(new STCCacheStructureIconsPacket(StructureLoadUtils.findStructureIcons()), event.getPlayer());
        }
    }
}