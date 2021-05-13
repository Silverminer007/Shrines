package com.silverminer.shrines.utils.network.proxy;

import java.io.File;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class ServerProxy implements IProxy {

	@Override
	public File getBaseDir() {
		return ((MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER)).getFile("");
	}

}