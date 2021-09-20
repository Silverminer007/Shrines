package com.silverminer.shrines.client.gui.packets;

import com.google.common.collect.Lists;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;
import com.silverminer.shrines.utils.network.CTSAddedStructurePacketPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class AddStructurePacketScreen extends NameStructurePacketScreen {

	protected AddStructurePacketScreen(Screen lastScreen) {
		super(lastScreen);
	}

	@Override
	public void done() {
		StructuresPacket packet = new StructuresPacket(this.nameField.getValue(), Lists.newArrayList(),
				false, this.minecraft.player.getName().getString());
		this.minecraft.setScreen(new WorkingScreen());
		ShrinesPacketHandler
				.sendToServer(new CTSAddedStructurePacketPacket(packet, this.minecraft.player.getUUID()));
	}

	@Override
	public ITextComponent defaultNameFieldString() {
		return StringTextComponent.EMPTY;
	}
}