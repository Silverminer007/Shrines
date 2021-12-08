package com.silverminer.shrines.gui.misc;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;

public interface IUpdatableScreen extends ContainerEventHandler {
    void updateButtonStatus(boolean flag);
    Screen getScreen();
}
