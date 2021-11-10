package com.silverminer.shrines.gui.misc;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;

public interface IUpdatableScreen extends INestedGuiEventHandler {
    void updateButtonStatus(boolean flag);
    Screen getScreen();
}
