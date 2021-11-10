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
import com.silverminer.shrines.gui.misc.IUpdatableScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
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
 *
 */
@OnlyIn(Dist.CLIENT)
public class EditPoolsList extends ExtendedList<EditPoolsList.Entry> {
	protected static final Logger LOGGER = LogManager.getLogger(EditPoolsList.class);
	protected final IUpdatableScreen screen;
	protected final StructuresPacket packet;

	public EditPoolsList(Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                         int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search, StructuresPacket packet, IUpdatableScreen screen) {
		super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
		this.screen = screen;
		this.packet = packet;
	}

	public void refreshList(Supplier<String> search) {
		this.clearEntries();

		List<TemplatePool> pools = packet.getPools();
		Collections.sort(pools);
		String s = search.get().toLowerCase(Locale.ROOT);

		for (TemplatePool pool : pools) {
			if (pool.getName().toString().toLowerCase(Locale.ROOT).contains(s)) {
				this.addEntry(new EditPoolsList.Entry(pool));
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

	public void setSelected(@Nullable EditPoolsList.Entry entry) {
		this.screen.updateButtonStatus(true);
		super.setSelected(entry);
	}

	public Optional<EditPoolsList.Entry> getSelectedOpt() {
		return Optional.ofNullable(this.getSelected());
	}

	@OnlyIn(Dist.CLIENT)
	public class Entry extends ExtendedList.AbstractListEntry<EditPoolsList.Entry> {
		protected final Minecraft minecraft;
		protected final TemplatePool pool;
		protected long lastClickTime;

		public Entry(TemplatePool pool) {
			this.pool = pool;
			this.minecraft = Minecraft.getInstance();
		}

		@ParametersAreNonnullByDefault
		public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
				int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			StringTextComponent header = new StringTextComponent(pool.getName().toString());
			String s1 = "Entries: " + this.pool.getEntries().size();

			this.minecraft.font.draw(ms, header, left, top + 1, 0xffffff);
			this.minecraft.font.draw(ms, s1, left, top + 9 + 3, 0x808080);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
			EditPoolsList.this.setSelected(this);
			if (Util.getMillis() - this.lastClickTime < 250L) {
				this.configure();
				return true;
			} else {
				this.lastClickTime = Util.getMillis();
				return false;
			}
		}

		public void configure() {
			this.minecraft.setScreen(new ConfigurePoolScreen(EditPoolsList.this.screen.getScreen(), EditPoolsList.this.packet, this.getPool()));
		}

		public TemplatePool getPool(){
			return this.pool;
		}
	}
}