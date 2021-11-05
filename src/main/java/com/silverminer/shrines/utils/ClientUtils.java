package com.silverminer.shrines.utils;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.gui.config.GeneralSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ClientUtils {
    public static final ResourceLocation BACK_BUTTON_TEXTURE = new ResourceLocation(ShrinesMod.MODID,
            "textures/gui/widgets.png");
    /**
     * This holds screen open keybind. It's initialised in ClientEvents
     * (FMLClientSetupEvent) and used in Key Pressed Event. Allows key rebinds
     */
    public static KeyBinding structuresScreen;

    public static Screen getConfigGui(Minecraft mc, Screen parent) {
        return new GeneralSettingsScreen(parent, Lists.newArrayList(Config.SERVER_SETTINGS_CONFIG));
    }

    public static void showErrorToast(ITextComponent errorMessage) {
        showErrorToast(new StringTextComponent("Shrines Error"), errorMessage);
    }

    public static void showErrorToast(String errorTitle, String errorMessage) {
        if (errorTitle.isEmpty()) {
            showErrorToast(new StringTextComponent(errorMessage));
        } else {
            showErrorToast(new StringTextComponent(errorTitle), new StringTextComponent(errorMessage));
        }
    }

    public static void showErrorToast(ITextComponent errorTitle, ITextComponent errorMessage) {
        Minecraft.getInstance().getToasts().addToast(SystemToast.multiline(Minecraft.getInstance(), null, errorTitle, errorMessage));
    }
}