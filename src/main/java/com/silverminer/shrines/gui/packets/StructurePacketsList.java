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
package com.silverminer.shrines.gui.packets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSDeletedStructurePacketPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Silverminer
 *
 */
@OnlyIn(Dist.CLIENT)
public class StructurePacketsList extends ExtendedList<StructurePacketsList.Entry> {
	protected static final Logger LOGGER = LogManager.getLogger();
	private final StructuresPacketsScreen screen;
	private final ArrayList<StructuresPacket> unfilteredPackets;

	public StructurePacketsList(StructuresPacketsScreen screen, Minecraft mc, int p_i49846_3_, int p_i49846_4_,
			int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search,
			ArrayList<StructuresPacket> list) {
		super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
		this.screen = screen;
		this.unfilteredPackets = list;

		this.refreshList(search);
	}

	public void refreshList(Supplier<String> search) {
		this.clearEntries();

		Collections.sort(this.unfilteredPackets);
		String s = search.get().toLowerCase(Locale.ROOT);

		for (StructuresPacket packet : this.unfilteredPackets) {
			if (packet.getDisplayName().toLowerCase(Locale.ROOT).contains(s)) {
				this.addEntry(new StructurePacketsList.Entry(packet));
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

	public void setSelected(@Nullable StructurePacketsList.Entry entry) {
		super.setSelected(entry);
		this.screen.updateButtonStatus(entry != null);
	}

	public Optional<StructurePacketsList.Entry> getSelectedOpt() {
		return Optional.ofNullable(this.getSelected());
	}

	@OnlyIn(Dist.CLIENT)
	public final class Entry extends ExtendedList.AbstractListEntry<StructurePacketsList.Entry> {
		private final Minecraft minecraft;
		private final StructuresPacket packet;
		private long lastClickTime;

		public Entry(StructuresPacket packet) {
			this.packet = packet;
			this.minecraft = Minecraft.getInstance();
		}

		@ParametersAreNonnullByDefault
		public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
				int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			String header = packet.getDisplayName() + " (" + packet.getSaveName() + ")";
			String s1 = new TranslationTextComponent("gui.shrines.author").getString() + ": " + this.packet.getAuthor();
			String s2 = new TranslationTextComponent("gui.shrines.structures").getString() + ": " + this.packet.getStructures().size() + " " + new TranslationTextComponent("gui.shrines.templates").getString() + ": " + this.packet.getStructures().size() + " " + new TranslationTextComponent("gui.shrines.pools").getString() + ": " + this.getPacket().getPools().size();

			this.minecraft.font.draw(ms, header, left, top + 1, this.packet.hasIssues ? 0xff0000 : 0xffffff);
			this.minecraft.font.draw(ms, s1, left, top + 9 + 3, 8421504);
			this.minecraft.font.draw(ms, s2, left, top + 9 + 9 + 3, 8421504);
		}

		public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
			StructurePacketsList.this.setSelected(this);
			if (Util.getMillis() - this.lastClickTime < 250L) {
				this.configure();
				return true;
			} else {
				this.lastClickTime = Util.getMillis();
				return false;
			}
		}

		public void configure() {
			this.minecraft.setScreen(new EditStructuresScreen(this.packet));
		}

		public void remove() {
			this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
				if (confirmed) {
					this.minecraft.setScreen(new WorkingScreen());
					ShrinesPacketHandler.sendToServer(new CTSDeletedStructurePacketPacket(this.packet.getSaveName()));
					this.minecraft.setScreen(new WorkingScreen());
				}

				this.minecraft.setScreen(StructurePacketsList.this.screen);
			}, new TranslationTextComponent("gui.shrines.removeQuestion", this.packet.getDisplayName()),
					new TranslationTextComponent("gui.shrines.removeWarning"),
					new TranslationTextComponent("gui.shrines.delete"), DialogTexts.GUI_CANCEL));
		}

		public StructuresPacket getPacket() {
			return this.packet;
		}
	}
}