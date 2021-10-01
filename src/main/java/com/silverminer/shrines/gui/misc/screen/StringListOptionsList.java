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
package com.silverminer.shrines.gui.misc.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.buttons.BooleanValueButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Silverminer
 *
 */
@OnlyIn(Dist.CLIENT)
public class StringListOptionsList extends ExtendedList<StringListOptionsList.Entry> {
	protected static final Logger LOGGER = LogManager.getLogger();
	private final StringListOptionsScreen screen;
	@Nullable
	private List<String> options;

	public StringListOptionsList(StringListOptionsScreen screen, Minecraft mc, int p_i49846_3_, int p_i49846_4_,
			int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search) {
		super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
		this.screen = screen;

		this.refreshList(search);
	}

	public void refreshList(Supplier<String> search) {
		this.clearEntries();

		this.options = this.screen.possibleValues;
		Collections.sort(this.options);
		String s = search.get().toLowerCase(Locale.ROOT);

		for (String opt : this.options) {
			if (opt.toLowerCase(Locale.ROOT).contains(s)) {
				this.addEntry(new StringListOptionsList.Entry(opt, this.screen.selectedValues.contains(opt)));
			}
		}

	}

	protected int getScrollbarPosition() {
		return this.width - 5;
	}

	public int getRowWidth() {
		return this.width - 10;
	}

	protected boolean isFocused() {
		return this.screen.getFocused() == this;
	}

	public void setSelected(@Nullable StringListOptionsList.Entry entry) {
		super.setSelected(entry);
	}

	public Optional<StringListOptionsList.Entry> getSelectedOpt() {
		return Optional.ofNullable(this.getSelected());
	}

	@OnlyIn(Dist.CLIENT)
	public final class Entry extends ExtendedList.AbstractListEntry<StringListOptionsList.Entry>
			implements INestedGuiEventHandler {
		@Nullable
		private IGuiEventListener focused;
		private boolean dragging;
		protected ArrayList<IGuiEventListener> children = Lists.newArrayList();
		private final Minecraft minecraft;
		protected final BooleanValueButton button;
		private final String opt;
		private boolean active;

		public Entry(String option, boolean active) {
			this.opt = option;
			this.active = active;
			this.minecraft = Minecraft.getInstance();
			this.button = new BooleanValueButton(0, 0, StringTextComponent.EMPTY, (button) -> {
				this.setActive(((BooleanValueButton) button).value);
				if (this.active) {
					StringListOptionsList.this.screen.selectedValues.add(opt);
				} else {
					StringListOptionsList.this.screen.selectedValues.remove(opt);
				}
			}, active);
			this.children.add(this.button);
		}

		private void setActive(boolean active) {
			this.active = active;
		}

		@Override
		public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
				boolean isHot, float partialTicks) {
			int descriptionTop = top + (StringListOptionsList.this.itemHeight - minecraft.font.lineHeight) / 2;
			minecraft.font.drawShadow(ms, this.opt, left, descriptionTop, 16777215);
			this.button.x = left + (width / 2);
			this.button.y = top + (StringListOptionsList.this.itemHeight - this.button.getHeight()) / 2;
			this.button.render(ms, mouseX, mouseY, partialTicks);
		}

		@Override
		public List<? extends IGuiEventListener> children() {
			return children;
		}

		public boolean isDragging() {
			return this.dragging;
		}

		public void setDragging(boolean p_231037_1_) {
			this.dragging = p_231037_1_;
		}

		public void setFocused(@Nullable IGuiEventListener p_231035_1_) {
			this.focused = p_231035_1_;
		}

		@Nullable
		public IGuiEventListener getFocused() {
			return this.focused;
		}
	}
}