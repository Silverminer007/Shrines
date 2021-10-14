package com.silverminer.shrines.mixins;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.StructureLoadUtils;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.codec.DatapackCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(method = "configurePackRepository", at = @At(value = "HEAD"))
    private static void onConfigurePackRepository(ResourcePackList p_240772_0_, DatapackCodec p_240772_1_, boolean p_240772_2_, CallbackInfoReturnable<DatapackCodec> callback) {
        p_240772_0_.addPackFinder(StructureLoadUtils.getPackFinder());
    }
}