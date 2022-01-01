package com.silverminer.shrines.utils;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.gui.config.NewGeneralSettingsScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class ClientUtils {
   public static final ResourceLocation BACK_BUTTON_TEXTURE = new ResourceLocation(ShrinesMod.MODID,
         "textures/gui/widgets.png");
   /**
    * This holds screen open keybind. It's initialised in ClientEvents
    * (FMLClientSetupEvent) and used in Client Tick Event. Allows key rebinds
    */
   public static KeyMapping editPackagesKeyMapping;
   public static KeyMapping openNovelsKeyMapping;

   public static Screen getConfigGui(Minecraft mc, Screen parent) {
      return new NewGeneralSettingsScreen(parent);
   }

   public static void showErrorToast(Component errorMessage) {
      showErrorToast(new TextComponent("Shrines Error"), errorMessage);
   }

   public static void showErrorToast(CalculationError error) {
      showErrorToast(error.header(), error.text());
   }

   public static void showErrorToast(String errorTitle, String errorMessage) {
      if (errorTitle.isEmpty()) {
         showErrorToast(new TextComponent(errorMessage));
      } else {
         showErrorToast(new TextComponent(errorTitle), new TextComponent(errorMessage));
      }
   }

   public static void showErrorToast(Component errorTitle, Component errorMessage) {
      Minecraft.getInstance().getToasts().addToast(SystemToast.multiline(Minecraft.getInstance(), SystemToast.SystemToastIds.PACK_LOAD_FAILURE, errorTitle, errorMessage));
      // SystemToastID isn't used so just give a dummy value
   }
}