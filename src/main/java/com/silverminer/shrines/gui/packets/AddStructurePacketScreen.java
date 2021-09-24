package com.silverminer.shrines.gui.packets;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSAddedStructurePacketPacket;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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