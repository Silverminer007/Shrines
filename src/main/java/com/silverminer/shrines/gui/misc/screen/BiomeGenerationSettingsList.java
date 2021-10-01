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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Silverminer
 *
 */
@OnlyIn(Dist.CLIENT)
public class BiomeGenerationSettingsList extends ExtendedList<BiomeGenerationSettingsList.Entry> {
	protected static final Logger LOGGER = LogManager.getLogger();
	private final BiomeGenerationSettingsScreen screen;
	@Nullable
	private List<String> options;

	public BiomeGenerationSettingsList(BiomeGenerationSettingsScreen screen, Minecraft mc, int p_i49846_3_,
			int p_i49846_4_, int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search) {
		super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
		this.screen = screen;

		this.refreshList(search);
	}

	public void refreshList(Supplier<String> search) {
		this.clearEntries();

		ArrayList<Biome.Category> possibleCategories = Lists.newArrayList(Biome.Category.values());
		ArrayList<String> selectedCategories = this.screen.selectedBiomeCategories;
		ArrayList<Biome> possibleBiomes = Lists.newArrayList(ForgeRegistries.BIOMES.getEntries().stream()
				.map(entry -> entry.getValue()).collect(Collectors.toList()));
		ArrayList<String> selectedBiomes = this.screen.selectedBiomes;

		String s = search.get().toLowerCase(Locale.ROOT);

		for (Biome.Category opt : possibleCategories) {
			if (opt.getName().toLowerCase(Locale.ROOT).contains(s)) {
				boolean selected = selectedCategories.contains(opt.toString());
				this.addEntry(new BiomeGenerationSettingsList.CategoryEntry(opt, selected));
				if (selected) {
					for (Biome opt1 : possibleBiomes.stream().filter(biome -> biome.getBiomeCategory().equals(opt))
							.collect(Collectors.toList())) {
						boolean selected1 = !selectedBiomes.contains(opt1.toString());
						this.addEntry(new BiomeGenerationSettingsList.BiomeEntry(opt1, selected1));
					}
				}
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

	public void setSelected(@Nullable BiomeGenerationSettingsList.Entry entry) {
		super.setSelected(entry);
	}

	public Optional<BiomeGenerationSettingsList.Entry> getSelectedOpt() {
		return Optional.ofNullable(this.getSelected());
	}

	@OnlyIn(Dist.CLIENT)
	public abstract class Entry extends ExtendedList.AbstractListEntry<BiomeGenerationSettingsList.Entry>
			implements INestedGuiEventHandler {
		@Nullable
		private IGuiEventListener focused;
		private boolean dragging;
		protected ArrayList<IGuiEventListener> children = Lists.newArrayList();
		protected final Minecraft minecraft;
		protected final BooleanValueButton button;
		protected final String opt;

		public Entry(String option, boolean active) {
			this.opt = option;
			this.minecraft = Minecraft.getInstance();
			this.button = new BooleanValueButton(0, 0, StringTextComponent.EMPTY, (button) -> {
				this.updateList(((BooleanValueButton) button).value);
				BiomeGenerationSettingsList.this
						.refreshList(() -> BiomeGenerationSettingsList.this.screen.searchBox.getValue());
			}, active);
			this.children.add(this.button);
		}

		public abstract void updateList(boolean active);

		@Override
		public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
				boolean isHot, float partialTicks) {
			int descriptionTop = top + (BiomeGenerationSettingsList.this.itemHeight - minecraft.font.lineHeight) / 2;
			minecraft.font.drawShadow(ms, this.opt, left, descriptionTop, this.isCategoryOption() ? 0xffffff : 0x999999);
			this.button.x = left + (width / 2);
			this.button.y = top + (BiomeGenerationSettingsList.this.itemHeight - this.button.getHeight()) / 2;
			this.button.render(ms, mouseX, mouseY, partialTicks);
		}

		public abstract boolean isCategoryOption();

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

	@OnlyIn(Dist.CLIENT)
	public class CategoryEntry extends Entry {

		public CategoryEntry(Biome.Category option, boolean active) {
			super(option.toString(), active);
		}

		@Override
		public void updateList(boolean active) {
			if (active) {
				BiomeGenerationSettingsList.this.screen.selectedBiomeCategories.add(this.opt);
				for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
					if (biome.getBiomeCategory().toString().equals(this.opt)) {
						BiomeGenerationSettingsList.this.screen.selectedBiomes.remove(biome.getRegistryName().toString());
					}
				}
			} else {
				BiomeGenerationSettingsList.this.screen.selectedBiomeCategories.remove(this.opt);
			}
		}

		@Override
		public boolean isCategoryOption() {
			return true;
		}

	}

	@OnlyIn(Dist.CLIENT)
	public class BiomeEntry extends Entry {

		public BiomeEntry(Biome option, boolean active) {
			super(option.getRegistryName().toString(), active);
		}

		@Override
		public void updateList(boolean active) {
			if (active) {
				BiomeGenerationSettingsList.this.screen.selectedBiomes.remove(this.opt);
			} else {
				BiomeGenerationSettingsList.this.screen.selectedBiomes.add(this.opt);
			}
		}

		@Override
		public boolean isCategoryOption() {
			return false;
		}

	}
}