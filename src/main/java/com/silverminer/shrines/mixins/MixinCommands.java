package com.silverminer.shrines.mixins;

import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Commands.class)
public class MixinCommands {
   @Shadow @Final private static Logger LOGGER;

   @Inject(method = "performCommand", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;isDebugEnabled()Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
   private void shrines_performCommand(CommandSourceStack pSource, String pCommand, CallbackInfoReturnable<Integer> cir, StringReader stringreader, Exception exception, MutableComponent mutablecomponent) {
      if(!LOGGER.isDebugEnabled()){
         LOGGER.error("Failed to execute command. Exception: ", exception);
      }
   }
}