package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.TemplatePool;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSAddTemplatePoolPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class ConfigurePoolScreen extends EditStructurePacketScreen {
    private final TemplatePool pool;
    protected Screen lastScreen;
    protected ConfigurePoolList poolsList;

    public ConfigurePoolScreen(Screen lastScreen, StructuresPacket packet, TemplatePool pool) {
        super(new TranslationTextComponent("gui.shrines.pools.configure_pool"), packet, false);
        this.lastScreen = lastScreen;
        this.pool = pool;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(new WorkingScreen());
            ShrinesPacketHandler.sendToServer(new CTSAddTemplatePoolPacket(this.pool, this.packet.getSaveName()));
        }
    }

    @Override
    protected void init() {
        super.init();
        this.headerheight = 26;
        this.delete.setMessage(new TranslationTextComponent("gui.shrines.remove"));
        this.poolsList = new ConfigurePoolList(minecraft, this.width, this.height, this.headerheight,
                this.bottomheight, 23, () -> this.searchBox.getValue(), packet, this, pool);
        this.children.add(poolsList);
        this.updateButtonStatus();
        this.refreshList(this.searchBox.getValue());
    }

    @Override
    protected void refreshList(String s) {
        this.poolsList.refreshList(() -> s);
    }

    @Override
    protected void add() {
        this.minecraft.setScreen(new AddTemplateToPoolScreen(this, this.packet, this.pool));
    }

    @Override
    protected void delete() {
        this.poolsList.getSelectedOpt().ifPresent(entry -> this.pool.getEntries().remove(entry.getPoolEntry()));
        this.refreshList(this.searchBox.getValue());
    }

    @Override
    protected void renameOrConfigure() {
        this.poolsList.getSelectedOpt().ifPresent(entry -> {
            entry.configure();
        });
    }

    @Override
    public void updateButtonStatus() {
        super.updateButtonStatus(this.poolsList.getSelectedOpt().isPresent());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
        this.poolsList.render(ms, mouseX, mouseY, p_230430_4_);
        super.render(ms, mouseX, mouseY, p_230430_4_);
    }
}
