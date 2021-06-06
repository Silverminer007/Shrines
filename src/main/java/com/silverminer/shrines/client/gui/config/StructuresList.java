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
package com.silverminer.shrines.client.gui.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.client.gui.config.options.ConfigStructureScreen;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Silverminer
 *
 */
@OnlyIn(Dist.CLIENT)
public class StructuresList extends ExtendedList<StructuresList.Entry> {
	protected static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");
	private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation(
			"textures/gui/world_selection.png");
	private final ShrinesStructuresScreen screen;
	@Nullable
	private List<IStructureConfig> structures;

	public StructuresList(ShrinesStructuresScreen screen, Minecraft mc, int p_i49846_3_, int p_i49846_4_,
			int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search, @Nullable StructuresList list) {
		super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
		this.screen = screen;

		this.refreshList(search);
	}

	public void refreshList(Supplier<String> search) {
		this.clearEntries();
		List<AbstractStructure<NoFeatureConfig>> structs = NewStructureInit.STRUCTURES.values().stream()
				.collect(Collectors.toList());
		structs.removeIf(struct -> struct == null);
		this.structures = structs.stream().map((config) -> {
			AbstractStructure<?> st = (AbstractStructure<?>) config.getStructure();
			return st.getConfig();
		}).collect(Collectors.toList());
		this.structures.removeIf(entry -> entry == null);
		for (CustomStructureData d : Utils.customsStructs)
			if (!this.structures.contains(d))
				this.structures.add(d);

		Collections.sort(this.structures);

		String s = search.get().toLowerCase(Locale.ROOT);

		for (IStructureConfig cfg : this.structures) {
			if (cfg.getName().toLowerCase(Locale.ROOT).contains(s)) {
				this.addEntry(new StructuresList.Entry(this, cfg));
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

	public void setSelected(@Nullable StructuresList.Entry entry) {
		super.setSelected(entry);
		this.screen.updateButtonStatus(true, !entry.getConfig().isBuiltIn());
	}

	public Optional<StructuresList.Entry> getSelectedOpt() {
		return Optional.ofNullable(this.getSelected());
	}

	public ShrinesStructuresScreen getScreen() {
		return this.screen;
	}

	@OnlyIn(Dist.CLIENT)
	public final class Entry extends ExtendedList.AbstractListEntry<StructuresList.Entry> implements AutoCloseable {
		public final ITextComponent ACTIVE = new TranslationTextComponent("gui.shrines.structures.list.active").withStyle(Style.EMPTY.withColor(Color.fromRgb(0x00ff00)));
		public final ITextComponent INACTIVE = new TranslationTextComponent("gui.shrines.structures.list.inactive").withStyle(Style.EMPTY.withColor(Color.fromRgb(0xff0000)));
		private final Minecraft minecraft;
		private final ShrinesStructuresScreen screen;
		private final IStructureConfig config;
		private long lastClickTime;

		public Entry(StructuresList p_i242066_2_, IStructureConfig cfg) {
			this.screen = p_i242066_2_.getScreen();
			this.config = cfg;
			this.minecraft = Minecraft.getInstance();
			// TODO Icons for structure entries
		}

		public IStructureConfig getConfig() {
			return this.config;
		}

		@SuppressWarnings("deprecation")
		public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_,
				int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			ITextComponent header = new StringTextComponent(config.getName() + " ").append(config.getGenerate() ? ACTIVE : INACTIVE);
			String s1 = config.getDimensions().toString();
			String s2 = "Seed:" + config.getSeed() + "  Distance:" + config.getDistance() + "  Seperation:"
					+ config.getSeparation();

			this.minecraft.font.draw(p_230432_1_, header, (float) (p_230432_4_ + 32 + 3), (float) (p_230432_3_ + 1),
					16777215);
			this.minecraft.font.draw(p_230432_1_, s1, (float) (p_230432_4_ + 32 + 3), (float) (p_230432_3_ + 9 + 3),
					8421504);
			this.minecraft.font.draw(p_230432_1_, s2, (float) (p_230432_4_ + 32 + 3), (float) (p_230432_3_ + 9 + 9 + 3),
					8421504);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bind(StructuresList.ICON_MISSING);
			RenderSystem.enableBlend();
			AbstractGui.blit(p_230432_1_, p_230432_4_, p_230432_3_, 0.0F, 0.0F, 32, 32, 32, 32);
			RenderSystem.disableBlend();
			if (this.minecraft.options.touchscreen || p_230432_9_) {
				this.minecraft.getTextureManager().bind(StructuresList.ICON_OVERLAY_LOCATION);
				AbstractGui.fill(p_230432_1_, p_230432_4_, p_230432_3_, p_230432_4_ + 32, p_230432_3_ + 32,
						-1601138544);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				int i = p_230432_7_ - p_230432_4_;
				boolean flag = i < 32;
				int j = flag ? 32 : 0;
				AbstractGui.blit(p_230432_1_, p_230432_4_, p_230432_3_, 0.0F, (float) j, 32, 32, 256, 256);
			}

		}

		public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
			StructuresList.this.setSelected(this);
			this.screen.updateButtonStatus(StructuresList.this.getSelectedOpt().isPresent(),
					!this.getConfig().isBuiltIn());
			if (p_231044_1_ - (double) StructuresList.this.getRowLeft() <= 32.0D) {
				this.configure();
				return true;
			} else if (Util.getMillis() - this.lastClickTime < 250L) {
				this.configure();
				return true;
			} else {
				this.lastClickTime = Util.getMillis();
				return false;
			}
		}

		public void configure() {
			this.minecraft.setScreen(new ConfigStructureScreen(this.screen, this.config, false));
		}

		public void remove() {
			this.minecraft.setScreen(new ConfirmScreen((confirmed) -> {
				if (confirmed) {
					this.minecraft.setScreen(new WorkingScreen());
					if (!(this.getConfig() instanceof CustomStructureData)) {
						LOGGER.error("You can only remove custom structures! You tried to remove {}",
								this.getConfig().getName());
						return;
					}
					CustomStructureData config = (CustomStructureData) this.getConfig();
					if (!Utils.customsStructs.remove(config))
						LOGGER.error("Failed to delete custom structure");
					Utils.customsToDelete.add(config.getName());
					NewStructureInit.STRUCTURES.remove(config.getDataName());

					StructuresList.this.refreshList(() -> {
						return this.screen.searchBox.getValue();
					});
				}

				this.minecraft.setScreen(this.screen);
			}, new TranslationTextComponent("gui.shrines.structures.removeQuestion", this.getConfig().getName()),
					new TranslationTextComponent("gui.shrines.structures.removeWarning"),
					new TranslationTextComponent("gui.shrines.structures.deleteButton"), DialogTexts.GUI_CANCEL));
		}

		public void disable() {
			this.config.setActive(!this.config.getActive());
		}

		@Override
		public void close() {
		}
	}
}