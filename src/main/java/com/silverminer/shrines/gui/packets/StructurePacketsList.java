/**
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.CTSDeletedStructurePacketPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
	@Nullable
	private ArrayList<StructuresPacket> packets;
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
			if (packet.getName().toLowerCase(Locale.ROOT).contains(s)) {
				this.addEntry(new StructurePacketsList.Entry(this, packet));
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

		public Entry(StructurePacketsList p_i242066_2_, StructuresPacket packet) {
			this.packet = packet;
			this.minecraft = Minecraft.getInstance();
		}

		public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
				int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			ITextComponent header = new StringTextComponent(packet.getName());
			String s1 = "Author: " + this.packet.getAuthor();
			String s2 = "Structures: " + this.packet.getStructures().size() + "  Templates:" + "" + "  Pools:" + "";

			this.minecraft.font.draw(ms, header, left, top + 1, 16777215);
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
			// TODO Open next screen
		}

		public void remove() {
			this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
				if (confirmed) {
					this.minecraft.setScreen(new WorkingScreen());
					ShrinesPacketHandler.sendToServer(new CTSDeletedStructurePacketPacket(this.packet.getTempID(),
							this.minecraft.player.getUUID()));
					this.minecraft.setScreen(new WorkingScreen());
				}

				this.minecraft.setScreen(StructurePacketsList.this.screen);
			}, new TranslationTextComponent("gui.shrines.structures.removeQuestion", this.packet.getName()),
					new TranslationTextComponent("gui.shrines.structures.removeWarning"),
					new TranslationTextComponent("gui.shrines.structures.deleteButton"), DialogTexts.GUI_CANCEL));
		}
	
		public void rename() {
			this.minecraft.setScreen(new RenameStructurePacketScreen(StructurePacketsList.this.screen, packet));
		}
	}
}