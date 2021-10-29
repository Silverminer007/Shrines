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

    public AddStructurePacketScreen(Screen lastScreen) {
        super(lastScreen);
    }

    @Override
    public void done() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        StructuresPacket packet = new StructuresPacket(this.nameField.getValue(), null, Lists.newArrayList(),
                false, this.minecraft.player.getName().getString());
        this.minecraft.setScreen(new WorkingScreen());
        ShrinesPacketHandler
                .sendToServer(new CTSAddedStructurePacketPacket(packet));
    }

    @Override
    public ITextComponent defaultNameFieldString() {
        return StringTextComponent.EMPTY;
    }
}