package com.silverminer.shrines.utils.network.proxy;

import java.io.File;

import net.minecraft.client.Minecraft;

public class ClientProxy implements IProxy {

	@Override
	public File getBaseDir() {
		Minecraft mc = Minecraft.getInstance();
		return mc.gameDirectory;
	}
}