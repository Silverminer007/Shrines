package com.silverminer.shrines.client.gui.config.settings;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.client.gui.config.buttons.CheckboxButtonEx;
import com.silverminer.shrines.client.gui.config.buttons.ValidationStatusButton;
import com.silverminer.shrines.client.gui.config.options.SettingsBlacklistScreen;
import com.silverminer.shrines.config.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class GeneralSettingsScreen extends Screen {
	protected static final ITextComponent TITLE = new TranslationTextComponent("gui.shrines.structures.settings");
	private Screen parent;
	private List<ForgeConfigSpec> configSpecs;
	private ModOptionList optionList;

	private static final int PADDING = 5;

	public GeneralSettingsScreen(Screen parent, List<ForgeConfigSpec> configSpecs) {
		super(TITLE);

		this.parent = parent;
		this.configSpecs = configSpecs;
	}

	@Override
	public void init(Minecraft mc, int width, int height) {
		super.init(mc, width, height);

		int titleHeight = mc.font.wordWrapHeight(title.getString(), width - 2 * PADDING);
		int paddedTitleHeight = titleHeight + PADDING * 2;

		addButton(width - 120 - 2 * PADDING, 0, 60, paddedTitleHeight, new StringTextComponent("Back"),
				button -> mc.setScreen(parent));
		addButton(width - 60 - PADDING, 0, 60, paddedTitleHeight, new StringTextComponent("Save"), button -> {
			this.optionList.commitChanges();
			for (ForgeConfigSpec spec : configSpecs)
				spec.save();

			mc.setScreen(parent);
		});

		int optionListHeaderHeight = titleHeight + 2 * PADDING;
		this.optionList = new ModOptionList(configSpecs, minecraft, width, height, optionListHeaderHeight,
				height - optionListHeaderHeight, 26);
		this.children.add(optionList);
	}

	private void addButton(int x, int y, int width, int height, ITextComponent label, Button.IPressable pressHandler) {
		Button button = new ExtendedButton(x, y, width, height, label, pressHandler);

		children.add(button);
		buttons.add(button);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		this.optionList.render(ms, mouseX, mouseY, partialTicks);
		RenderSystem.disableLighting(); // Rendering the tooltip enables lighting but buttons etc. assume lighting to be
										// disabled.
		super.render(ms, mouseX, mouseY, partialTicks);
		minecraft.font.draw(ms, title.getString(), PADDING, PADDING, 16777215);
	}

	@Override
	public void tick() {
		super.tick();
		optionList.tick();
	}

	@OnlyIn(Dist.CLIENT)
	public class ModOptionList extends AbstractOptionList<ModOptionList.Entry> {
		private static final int LEFT_RIGHT_BORDER = 30;
		private static final String I18N_TOOLTIP_SUFFIX = ".tooltip";
		private static final String I18N_VALID = "config.input_valid";
		private static final String I18N_INVALID = "config.input_invalid";
		private static final String I18N_NEEDS_WORLD_RESTART = "config.needs_world_restart";

		public ModOptionList(List<ForgeConfigSpec> configSpecs, Minecraft mc, int width, int height, int top,
				int bottom, int itemHeight) {
			super(mc, width, height, top, bottom, itemHeight);

			for (ForgeConfigSpec spec : configSpecs) {
				UnmodifiableConfig configValues = spec.getValues();
				generateEntries(spec, configValues, "");
			}
		}

		@Override
		public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
			super.render(ms, mouseX, mouseY, partialTicks);

			String tooltip = null;

			for (Entry entry : this.children()) {
				tooltip = entry.getTooltip();

				if (!StringUtils.isNullOrEmpty(tooltip)) {
					List<ITextComponent> comment = Arrays.asList(tooltip.split("\n")).stream()
							.map(s -> new StringTextComponent(s)).collect(Collectors.toList());
					renderComponentTooltip(ms, comment, mouseX, mouseY);

					break;
				}
			}
		}

		public void tick() {
			for (IGuiEventListener child : this.children()) {
				((Entry) child).tick();
			}
		}

		@Override
		public int getRowWidth() {
			return width - LEFT_RIGHT_BORDER * 2;
		}

		@Override
		protected int getScrollbarPosition() {
			return width - LEFT_RIGHT_BORDER;
		}

		@Override
		public boolean mouseClicked(double x, double y, int button) {
			if (super.mouseClicked(x, y, button)) {
				IGuiEventListener focusedChild = getFocused();

				for (IGuiEventListener child : this.children()) {
					if (child != focusedChild)
						((Entry) child).clearFocus();
				}

				return true;
			}

			return false;
		}

		public void commitChanges() {
			for (Entry entry : this.children()) {
				entry.commitChanges();
			}
		}

		private void generateEntries(UnmodifiableConfig spec, UnmodifiableConfig values, String path) {
			String currentPath;

			for (UnmodifiableConfig.Entry entry : spec.entrySet()) {
				currentPath = (path.length() > 0) ? path + "." + entry.getKey() : entry.getKey();

				if (entry.getValue() instanceof com.electronwill.nightconfig.core.Config) {
					String i18nKey = "config." + entry.getKey();
					String categoryLabel = (I18n.exists(i18nKey)) ? I18n.get(i18nKey) : entry.getKey();

					addEntry(new CategoryEntry(categoryLabel));
					generateEntries(spec.get(entry.getKey()), values, currentPath);
				} else if (entry.getValue() instanceof ForgeConfigSpec.ValueSpec) {
					ForgeConfigSpec.ConfigValue<?> value = values.get(currentPath);
					ForgeConfigSpec.ValueSpec valueSpec = entry.getValue();

					addEntry(new OptionEntry(valueSpec, value));
				}
			}
		}

		@OnlyIn(Dist.CLIENT)
		public abstract class Entry extends AbstractOptionList.Entry<GeneralSettingsScreen.ModOptionList.Entry> {
			public abstract void clearFocus();

			public abstract void commitChanges();

			public abstract void tick();

			public abstract String getTooltip();
		}

		@OnlyIn(Dist.CLIENT)
		public class CategoryEntry extends Entry {
			private final String text;
			private final int width;

			public CategoryEntry(String text) {
				this.text = text;
				this.width = minecraft.font.width(text);
			}

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {
				minecraft.font.drawShadow(ms, this.text, minecraft.screen.width / 2 - this.width / 2,
						top + height - 9 - 1, 16777215);
			}

			@Override
			public List<? extends IGuiEventListener> children() {
				return Collections.emptyList();
			}

			@Override
			public boolean changeFocus(boolean forward) {
				return false;
			}

			@Override
			public void clearFocus() {
			}

			@Override
			public void commitChanges() {
			}

			@Override
			public void tick() {
			}

			@Override
			public String getTooltip() {
				return null;
			}
		}

		@OnlyIn(Dist.CLIENT)
		public class OptionEntry extends Entry {
			private ForgeConfigSpec.ValueSpec valueSpec;
			private ForgeConfigSpec.ConfigValue<?> configValue;
			private TextFieldWidget editBox;
			private CheckboxButtonEx checkBox;
			private Button furtherScreenButton;
			private ImageButton needsWorldRestartButton;
			private ValidationStatusButton validatedButton;
			private List<IGuiEventListener> children;
			private String tooltipText;

			public OptionEntry(ForgeConfigSpec.ValueSpec valueSpec, ForgeConfigSpec.ConfigValue<?> configValue) {
				this.valueSpec = valueSpec;
				this.configValue = configValue;

				this.validatedButton = new ValidationStatusButton(0, 0, button -> {
					if (this.editBox != null) {
						this.editBox.setValue("");
						this.editBox.insertText(this.valueSpec.getDefault().toString());
						this.editBox.setFocus(false);
					} else if (this.checkBox != null) {
						this.checkBox.value = (boolean) this.valueSpec.getDefault();
					}
				});

				this.needsWorldRestartButton = new ImageButton(0, 0, 15, 12, 182, 24, 0, Button.WIDGETS_LOCATION, 256,
						256, (b) -> {
						});
				this.needsWorldRestartButton.active = false;
				this.needsWorldRestartButton.visible = valueSpec.needsWorldRestart();

				Object value = configValue.get();

				if (value instanceof Boolean) {
					this.checkBox = new CheckboxButtonEx(0, 0, 20, 20, new StringTextComponent(""), (boolean) value);

					this.children = ImmutableList.of(this.validatedButton, this.needsWorldRestartButton, this.checkBox);
				} else if (value instanceof List<?>) {
					furtherScreenButton = new Button(0, 0, 100, 20,
							new TranslationTextComponent(this.valueSpec.getTranslationKey()), (button) -> {
								GeneralSettingsScreen.this.minecraft.setScreen(new SettingsBlacklistScreen(
										GeneralSettingsScreen.this, title, Config.SETTINGS.BLACKLISTED_BIOMES.get()));
							});
					this.children = ImmutableList.of(this.validatedButton, this.needsWorldRestartButton,
							this.furtherScreenButton);
				} else {
					this.editBox = new TextFieldWidget(minecraft.font, 0, 0, 100, itemHeight - PADDING,
							new StringTextComponent(""));
					this.editBox.setTextColor(16777215);
					this.editBox.insertText(value.toString());
					this.editBox.setMaxLength(256);
					this.editBox.setCanLoseFocus(true);
					this.editBox.setFilter(this::validateTextFieldInput);

					this.children = ImmutableList.of(this.validatedButton, this.needsWorldRestartButton, this.editBox);
				}

				this.tooltipText = null;
			}

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {
				this.validatedButton.x = getScrollbarPosition() - this.validatedButton.getWidth()
						- this.needsWorldRestartButton.getWidth() - 2 * PADDING;
				this.validatedButton.y = top + ((itemHeight - this.validatedButton.getHeight()) / 2) - 1;
				this.validatedButton.render(ms, mouseX, mouseY, partialTicks);

				// This needs to be here because the TextFieldWidget changes the GL state and
				// never sets it back,
				// nor does the ImageButton set the correct values to render properly. Without
				// this call, the
				// ImageButtons are just black after the first TextFieldWidget is rendered.
				// Update: No longer needed because the ValidationStatusButton sets up the state
				// correctly and is rendered
				// BEFORE this ImageButton. DON'T delete this comment to avoid confusion in the
				// future.
				// RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0f);

				this.needsWorldRestartButton.x = getScrollbarPosition() - this.needsWorldRestartButton.getWidth()
						- PADDING;
				this.needsWorldRestartButton.y = top + ((itemHeight - this.needsWorldRestartButton.getHeight()) / 2)
						- 1;
				this.needsWorldRestartButton.render(ms, mouseX, mouseY, partialTicks);

				if (this.editBox != null) {
					this.editBox.x = left + (width / 2) + PADDING;
					this.editBox.y = top;
					this.editBox.setWidth((width / 2) - this.validatedButton.getWidth()
							- this.needsWorldRestartButton.getWidth() - 4 * PADDING - 6);
					this.editBox.render(ms, mouseX, mouseY, partialTicks);
				} else if (this.checkBox != null) {
					this.checkBox.x = left + (width / 2) + PADDING;
					this.checkBox.y = top;
					this.checkBox.render(ms, mouseX, mouseY, partialTicks);
				} else if (this.furtherScreenButton != null) {
					this.furtherScreenButton.x = left + (width / 2) + PADDING;
					this.furtherScreenButton.y = top;
					this.furtherScreenButton.setWidth((width / 2) - this.validatedButton.getWidth()
							- this.needsWorldRestartButton.getWidth() - 4 * PADDING - 6);
					this.furtherScreenButton.render(ms, mouseX, mouseY, partialTicks);
				}

				// Getting translations during rendering is not exactly a smart thing to do, but
				// it's just the config UI so .. meh.
				String description = (I18n.exists(this.valueSpec.getTranslationKey()))
						? I18n.get(this.valueSpec.getTranslationKey())
						: this.valueSpec.getTranslationKey();
				int descriptionWidth = minecraft.font.width(description);
				int descriptionLeft = left + (width / 2) - descriptionWidth - PADDING;
				int descriptionTop = top + (itemHeight / 2) - PADDING - minecraft.font.lineHeight / 2 + 2;
				minecraft.font.drawShadow(ms, description, descriptionLeft, descriptionTop, 16777215);

				// Set tooltip to be rendered by the ModOptionList. This could be moved to
				// mouseMoved(), but either
				// the tooltip for the description text would have to stay here or its bounds
				// would have to be stored.
				// To not complicate things, keep everything here for now.
				if ((mouseX >= descriptionLeft) && (mouseX < (descriptionLeft + descriptionWidth))
						&& (mouseY >= descriptionTop) && (mouseY < (descriptionTop + minecraft.font.lineHeight))) {
					// Tooltip for the description
					String i18nTooltipKey = this.valueSpec.getTranslationKey() + I18N_TOOLTIP_SUFFIX;
					this.tooltipText = (I18n.exists(i18nTooltipKey)) ? I18n.get(i18nTooltipKey)
							: this.valueSpec.getComment();
				} else if ((mouseX >= this.validatedButton.x)
						&& (mouseX < (this.validatedButton.x + this.validatedButton.getWidth()))
						&& (mouseY >= this.validatedButton.y)
						&& (mouseY < (this.validatedButton.y + this.validatedButton.getHeight()))) {
					// Tooltip for the validation button.
					this.tooltipText = (this.validatedButton.isValid()) ? I18n.get(I18N_VALID) : I18n.get(I18N_INVALID);
				} else if (valueSpec.needsWorldRestart() && (mouseX >= this.needsWorldRestartButton.x)
						&& (mouseX < (this.needsWorldRestartButton.x + this.needsWorldRestartButton.getWidth()))
						&& (mouseY >= this.needsWorldRestartButton.y)
						&& (mouseY < (this.needsWorldRestartButton.y + this.needsWorldRestartButton.getHeight()))) {
					// Tooltip for the needs world restart button.
					this.tooltipText = I18n.get(I18N_NEEDS_WORLD_RESTART);
				} else {
					this.tooltipText = null;
				}
			}

			@Override
			public List<? extends IGuiEventListener> children() {
				return this.children;
			}

			@Override
			public void clearFocus() {
				if (this.editBox != null) {
					this.editBox.setFocus(false);
				}
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			public void commitChanges() {
				Object value = this.configValue.get();

				if (value instanceof Boolean) {
					ForgeConfigSpec.BooleanValue cfg = (ForgeConfigSpec.BooleanValue) this.configValue;
					cfg.set(this.checkBox.value);
				} else if(this.editBox != null) {
					String text = this.editBox.getValue();

					if (value instanceof Integer) {
						try {
							int parsedValue = Integer.parseInt(text);

							if (this.valueSpec.test(parsedValue)) {
								ForgeConfigSpec.IntValue cfg = (ForgeConfigSpec.IntValue) this.configValue;
								cfg.set(parsedValue);
							}
						} catch (NumberFormatException ex) {
						}
					} else if (value instanceof Long) {
						try {
							long parsedValue = Long.parseLong(text);

							if (this.valueSpec.test(parsedValue)) {
								ForgeConfigSpec.LongValue cfg = (ForgeConfigSpec.LongValue) this.configValue;
								cfg.set(parsedValue);
							}
						} catch (NumberFormatException ex) {
						}
					} else if (value instanceof Double) {
						try {
							double parsedValue = Double.parseDouble(text);

							if (this.valueSpec.test(parsedValue)) {
								ForgeConfigSpec.DoubleValue cfg = (ForgeConfigSpec.DoubleValue) this.configValue;
								cfg.set(parsedValue);
							}
						} catch (NumberFormatException ex) {
						}
					} else if (value instanceof String) {
						if (this.valueSpec.test(text)) {
							ForgeConfigSpec.ConfigValue<String> cfg = (ForgeConfigSpec.ConfigValue<String>) this.configValue;
							cfg.set(text);
						}
					}
				}
			}

			@Override
			public void tick() {
				if (this.editBox != null) {
					this.editBox.tick();
				}
			}

			@Override
			public String getTooltip() {
				return this.tooltipText;
			}

			// Sets the state of the ValidationStatusButton button based on the input in the
			// TextFieldWidget.
			private boolean validateTextFieldInput(String text) {
				Object value = this.configValue.get();

				if (value instanceof Integer) {
					try {
						int parsedValue = Integer.parseInt(text);
						this.validatedButton.setValid(this.valueSpec.test(parsedValue));
					} catch (NumberFormatException ex) {
						this.validatedButton.setInvalid();
					}
				} else if (value instanceof Long) {
					try {
						long parsedValue = Long.parseLong(text);
						this.validatedButton.setValid(this.valueSpec.test(parsedValue));
					} catch (NumberFormatException ex) {
						this.validatedButton.setInvalid();
					}
				} else if (value instanceof Double) {
					try {
						double parsedValue = Double.parseDouble(text);
						this.validatedButton.setValid(this.valueSpec.test(parsedValue));
					} catch (NumberFormatException ex) {
						this.validatedButton.setInvalid();
					}
				} else if (value instanceof String) {
					this.validatedButton.setValid(this.valueSpec.test(text));
				}

				return true;
			}
		}
	}
}
