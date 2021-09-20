package com.silverminer.shrines.client.gui.packets;

import com.silverminer.shrines.new_custom_structures.StructuresPacket;
import com.silverminer.shrines.utils.network.CTSRenamedStructurePacketPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RenameStructurePacketScreen extends NameStructurePacketScreen {
	protected final StructuresPacket packet;

	protected RenameStructurePacketScreen(Screen lastScreen, StructuresPacket packet) {
		super(lastScreen);
		this.packet = packet;
	}

	@Override
	public void done() {
		int IDtoDelete = this.packet.getTempID();
		StructuresPacket newPacket = this.packet.copy();
		newPacket.setName(this.nameField.getValue());
		this.minecraft.setScreen(new WorkingScreen());
		ShrinesPacketHandler
				.sendToServer(new CTSRenamedStructurePacketPacket(newPacket, this.minecraft.player.getUUID(), IDtoDelete));
	}

	@Override
	public ITextComponent defaultNameFieldString() {
		return new StringTextComponent(this.packet.getName());
	}

}