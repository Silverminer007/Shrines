/*
 * Silverminer (and Team)
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 *
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class ConfigurePoolList extends ExtendedList<ConfigurePoolList.Entry> {
    protected static final Logger LOGGER = LogManager.getLogger(ConfigurePoolList.class);
    private final ConfigurePoolScreen screen;
    private final StructuresPacket packet;
    private final TemplatePool templatePool;

    public ConfigurePoolList(Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                             int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search, StructuresPacket packet, ConfigurePoolScreen screen, TemplatePool templatePool) {
        super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
        this.screen = screen;
        this.packet = packet;
        this.templatePool = templatePool;

        this.refreshList(search);
    }

    public void refreshList(Supplier<String> search) {
        this.clearEntries();

        List<TemplatePool.Entry> templatePoolEntries = this.templatePool.getEntries();
        Collections.sort(templatePoolEntries);
        String s = search.get().toLowerCase(Locale.ROOT);

        for (TemplatePool.Entry entry : templatePoolEntries) {
            if (entry.getTemplate().toString().toLowerCase(Locale.ROOT).contains(s)) {
                this.addEntry(new ConfigurePoolList.Entry(entry));
            }
        }

    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 60;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 160;
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    public void setSelected(@Nullable ConfigurePoolList.Entry entry) {
        this.screen.updateButtonStatus(true);
        super.setSelected(entry);
    }

    public Optional<ConfigurePoolList.Entry> getSelectedOpt() {
        return Optional.ofNullable(this.getSelected());
    }

    @OnlyIn(Dist.CLIENT)
    public final class Entry extends ExtendedList.AbstractListEntry<ConfigurePoolList.Entry> {
        private final Minecraft minecraft;
        private final TemplatePool.Entry entry;
        private long lastClickTime;

        public Entry(TemplatePool.Entry entry) {
            this.entry = entry;
            this.minecraft = Minecraft.getInstance();
        }

        @ParametersAreNonnullByDefault
        public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                           int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            StringTextComponent header = new StringTextComponent(entry.getTemplate().toString());
            String s1 = "Weight: " + this.entry.getWeight() + "    " + (this.entry.isTerrain_matching() ? "Terrain Matching" : "Rigid");

            this.minecraft.font.draw(ms, header, left, top + 1, 0xffffff);
            this.minecraft.font.draw(ms, s1, left, top + 9 + 3, 0x808080);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
            ConfigurePoolList.this.setSelected(this);
            if (Util.getMillis() - this.lastClickTime < 250L) {
                this.configure();
                return true;
            } else {
                this.lastClickTime = Util.getMillis();
                return false;
            }
        }

        public void configure() {
            this.minecraft.setScreen(new ConfigurePoolEntryScreen(ConfigurePoolList.this.screen, this.getPoolEntry()));
        }

        public TemplatePool.Entry getPoolEntry() {
            return this.entry;
        }
    }
}