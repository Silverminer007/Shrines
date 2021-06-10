package com.silverminer.shrines.client.gui.config.options;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.client.gui.config.options.ConfigStructureScreen.ModOptionList.Entry;
import com.silverminer.shrines.client.gui.config.options.ConfigStructureScreen.ModOptionList.NameEntry;
import com.silverminer.shrines.client.gui.config.options.ConfigStructureScreen.ModOptionList.OptionEntry;
import com.silverminer.shrines.client.gui.config.resource.AddResourceScreen;
import com.silverminer.shrines.client.gui.config.widgets.buttons.CheckboxButtonEx;
import com.silverminer.shrines.client.gui.config.widgets.buttons.ValidationStatusButton;
import com.silverminer.shrines.config.IConfigOption;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.structures.custom.helper.ConfigOption;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.custom.helper.PieceData;
import com.silverminer.shrines.utils.StructureUtils;
import com.silverminer.shrines.utils.custom_structures.OptionParsingResult;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

@OnlyIn(Dist.CLIENT)
public class ConfigStructureScreen extends Screen {
	protected static final Logger LOG = LogManager.getLogger(ConfigStructureScreen.class);
	private Screen parent;
	private IStructureConfig configSpecs;
	private ModOptionList optionList;
	private static final TranslationTextComponent NEW_TITLE = new TranslationTextComponent(
			"gui.shrines.structures.add.title");
	private boolean isNew;
	private Button saveButton;

	private static final int PADDING = 5;

	public ConfigStructureScreen(Screen parent, IStructureConfig config, boolean isNew) {
		super(isNew ? NEW_TITLE
				: new TranslationTextComponent("gui.shrines.structures.config.title", config.getName()));
		this.parent = parent;
		this.configSpecs = config;
		this.isNew = isNew;
	}

	public IStructureConfig getConfig() {
		return this.configSpecs;
	}

	public void setConfig(IStructureConfig config) {
		this.configSpecs = config;
	}

	@Override
	public void init(Minecraft mc, int width, int height) {
		super.init(mc, width, height);
		IStructureConfig config = StructureUtils.getConfigOf(this.configSpecs.getName(), false);
		this.configSpecs = config != null ? config : this.configSpecs;

		int titleHeight = mc.font.wordWrapHeight(title.getString(), width - 2 * PADDING);
		int paddedTitleHeight = titleHeight + PADDING * 2;

		addButton(width - 120 - 2 * PADDING, 0, 60, paddedTitleHeight, new StringTextComponent("Back"),
				button -> mc.setScreen(parent));
		this.saveButton = addButton(width - 60 - PADDING, 0, 60, paddedTitleHeight, new StringTextComponent("Save"),
				button -> {
					this.optionList.commitChanges();
					// Only custom structures need special save;
					// Mods ones can save after every
					// change(with commitChanges)
					if (this.configSpecs instanceof CustomStructureData) {
						CustomStructureData csd = (CustomStructureData) this.configSpecs;
						for (Entry e : this.optionList.children()) {
							if (e instanceof NameEntry) {
								csd.name = ((NameEntry) e).data.getName();
							} else if (e instanceof OptionEntry) {
								OptionEntry oe = (OptionEntry) e;
								csd.fromString(oe.getOption().getName(), oe.getOption().getValue().toString());
							}
						}
						// TODO Check for need of syncing data from client to server
						if (this.isNew) {
							Utils.addStructure(csd, false);
						} else {
							Utils.replace(csd, false);
						}
						Utils.saveStructures();
					}
					mc.setScreen(parent);
				});

		int optionListHeaderHeight = titleHeight + 2 * PADDING;
		this.optionList = new ModOptionList(configSpecs, minecraft, width, height, optionListHeaderHeight,
				height - optionListHeaderHeight, 26, isNew);
		this.children.add(optionList);
		ConfigStructureScreen.this.updateValid();
	}

	private Button addButton(int x, int y, int width, int height, ITextComponent label,
			Button.IPressable pressHandler) {
		Button button = new ExtendedButton(x, y, width, height, label, pressHandler);

		children.add(button);
		buttons.add(button);
		return button;
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

	public void updateValid() {
		ConfigStructureScreen.this.saveButton.active = true;
		for (Entry entry : this.optionList.children()) {
			if (!entry.getIsValid()) {
				ConfigStructureScreen.this.saveButton.active = false;
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public class ModOptionList extends AbstractOptionList<ModOptionList.Entry> {
		private static final int LEFT_RIGHT_BORDER = 30;
		private static final String I18N_TOOLTIP_SUFFIX = ".tooltip";
		private static final String I18N_VALID = "config.input_valid";
		private static final String I18N_INVALID = "config.input_invalid";

		@SuppressWarnings("unchecked")
		public ModOptionList(IStructureConfig configSpecs, Minecraft mc, int width, int height, int top, int bottom,
				int itemHeight, boolean isNew) {
			super(mc, width, height, top, bottom, itemHeight);
			boolean isCustom = configSpecs instanceof CustomStructureData;
			CustomStructureData csd = null;
			if (isCustom) {
				csd = (CustomStructureData) configSpecs;
				if (isNew) {
					this.addEntry(new NameEntry(csd));
				}
			}
			for (IConfigOption<?> spec : configSpecs.getAllOptions()) {
				if (spec.getValue() instanceof Boolean)
					this.addEntry(new BooleanOptionEntry((IConfigOption<Boolean>) spec, configSpecs.getName()));
				else if (spec.getValue() instanceof List<?>) {
					if (spec instanceof ConfigOption<?> && isCustom) {
						ConfigOption<?> co = (ConfigOption<?>) spec;
						if (co.equals(csd.pieces)) {
							this.addEntry(new PiecesOptionEntry((ConfigOption<List<PieceData>>) co, csd.getName()));
							continue;
						}
					}
					this.addEntry(new ScreenedOptionEntry((IConfigOption<List<?>>) spec, configSpecs.getName()));
				} else
					this.addEntry(new TextFieldOptionEntry(spec));
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

		@OnlyIn(Dist.CLIENT)
		public abstract class Entry extends AbstractOptionList.Entry<ConfigStructureScreen.ModOptionList.Entry> {
			public abstract void clearFocus();

			public abstract void commitChanges();

			public abstract void tick();

			public abstract String getTooltip();

			public abstract boolean getIsValid();
		}

		@OnlyIn(Dist.CLIENT)
		public class NameEntry extends Entry {
			private TextFieldWidget editBox;
			private ValidationStatusButton validatedButton;
			private List<IGuiEventListener> children;
			private String tooltipText = "";
			private CustomStructureData data;

			public NameEntry(CustomStructureData csd) {
				this.data = csd;

				this.validatedButton = new ValidationStatusButton(0, 0, button -> {
					if (this.editBox != null) {
						this.editBox.setValue("");
						this.editBox.setFocus(false);
					}
					ConfigStructureScreen.this.updateValid();
				});
				this.validatedButton.setInvalid();

				this.editBox = new TextFieldWidget(minecraft.font, 0, 0, 100, itemHeight - PADDING,
						new StringTextComponent(""));
				this.editBox.setTextColor(16777215);
				this.editBox.insertText("");
				this.editBox.setMaxLength(256);
				this.editBox.setCanLoseFocus(true);
				this.editBox.setFilter(this::validateTextFieldInput);

				this.children = ImmutableList.of(this.validatedButton, this.editBox);
			}

			@Override
			public List<? extends IGuiEventListener> children() {
				return this.children;
			}

			private boolean validateTextFieldInput(String text) {
				String s = text;
				for (char c0 : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
					s = s.replace(c0, '_');
				}
				boolean flag = text.contains(" ") || !text.toLowerCase(Locale.ROOT).equals(text)
						|| !SharedConstants.filterText(text).equals(text) || !s.equals(text) || text.isEmpty();
				this.validatedButton.setValid(!flag);
				ConfigStructureScreen.this.updateValid();
				return true;
			}

			@Override
			public void clearFocus() {
				if (this.editBox != null) {
					this.editBox.setFocus(false);
				}
			}

			@Override
			public void commitChanges() {
				this.data.name = this.editBox.getValue();
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

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {
				this.validatedButton.x = getScrollbarPosition() - this.validatedButton.getWidth() - 2 * PADDING;
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

				if (this.editBox != null) {
					this.editBox.x = left + (width / 2) + PADDING;
					this.editBox.y = top;
					this.editBox.setWidth((width / 2) - this.validatedButton.getWidth() - 4 * PADDING - 6);
					this.editBox.render(ms, mouseX, mouseY, partialTicks);
				}

				// Getting translations during rendering is not exactly a smart thing to do, but
				// it's just the config UI so .. meh.
				String translationKey = "gui.shrines.config.name";
				String description = I18n.get(translationKey);
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
					String i18nTooltipKey = translationKey + I18N_TOOLTIP_SUFFIX;
					this.tooltipText = (I18n.exists(i18nTooltipKey)) ? I18n.get(i18nTooltipKey) : "";
				} else if ((mouseX >= this.validatedButton.x)
						&& (mouseX < (this.validatedButton.x + this.validatedButton.getWidth()))
						&& (mouseY >= this.validatedButton.y)
						&& (mouseY < (this.validatedButton.y + this.validatedButton.getHeight()))) {
					// Tooltip for the validation button.
					this.tooltipText = (this.validatedButton.isValid()) ? I18n.get(I18N_VALID) : I18n.get(I18N_INVALID);
				} else {
					this.tooltipText = null;
				}
			}

			@Override
			public boolean getIsValid() {
				return this.validatedButton.isValid();
			}
		}

		public abstract class OptionEntry extends Entry {
			public abstract IConfigOption<?> getOption();
		}

		@OnlyIn(Dist.CLIENT)
		public class BooleanOptionEntry extends OptionEntry {
			private IConfigOption<?> valueSpec;
			private CheckboxButtonEx checkBox;
			private ValidationStatusButton validatedButton;
			private List<IGuiEventListener> children;
			private String tooltipText;
			private String structure;

			public BooleanOptionEntry(IConfigOption<Boolean> valueSpec, String structure) {
				this.valueSpec = valueSpec;
				this.structure = structure;
				Boolean configValue = valueSpec.getValue();

				this.validatedButton = new ValidationStatusButton(0, 0, button -> {
					this.checkBox.value = (boolean) this.valueSpec.getDefaultValue();
				});

				this.checkBox = new CheckboxButtonEx(0, 0, 10, 20, new StringTextComponent(""), configValue);

				this.children = ImmutableList.of(this.validatedButton, this.checkBox);

				this.tooltipText = null;
			}

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {
				this.validatedButton.x = getScrollbarPosition() - this.validatedButton.getWidth() - 2 * PADDING;
				this.validatedButton.y = top + ((itemHeight - this.validatedButton.getHeight()) / 2) - 1;
				this.validatedButton.render(ms, mouseX, mouseY, partialTicks);

				this.checkBox.x = left + (width / 2) + PADDING;
				this.checkBox.y = top;
				this.checkBox.render(ms, mouseX, mouseY, partialTicks);

				// Getting translations during rendering is not exactly a smart thing to do, but
				// it's just the config UI so .. meh.
				String translationKey = "gui.shrines.config." + valueSpec.getName();
				String description = I18n.get(translationKey);
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
					String i18nTooltipKey = translationKey + I18N_TOOLTIP_SUFFIX;
					this.tooltipText = (I18n.exists(i18nTooltipKey)) ? I18n.get(i18nTooltipKey) : "";
				} else if ((mouseX >= this.validatedButton.x)
						&& (mouseX < (this.validatedButton.x + this.validatedButton.getWidth()))
						&& (mouseY >= this.validatedButton.y)
						&& (mouseY < (this.validatedButton.y + this.validatedButton.getHeight()))) {
					// Tooltip for the validation button.
					this.tooltipText = (this.validatedButton.isValid()) ? I18n.get(I18N_VALID) : I18n.get(I18N_INVALID);
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
			}

			@Override
			public void commitChanges() {
				this.valueSpec.fromString(String.valueOf(this.checkBox.getValue()),
						StructureUtils.getConfigOf(structure, false));
			}

			@Override
			public void tick() {
			}

			@Override
			public String getTooltip() {
				return this.tooltipText;
			}

			@Override
			public IConfigOption<?> getOption() {
				return this.valueSpec;
			}

			@Override
			public boolean getIsValid() {
				return this.validatedButton.isValid();
			}
		}

		@OnlyIn(Dist.CLIENT)
		public class TextFieldOptionEntry extends OptionEntry {
			private IConfigOption<?> valueSpec;
			private TextFieldWidget editBox;
			private ValidationStatusButton validatedButton;
			private List<IGuiEventListener> children;
			private String tooltipText;
			private Random rand = new Random();

			public TextFieldOptionEntry(IConfigOption<?> spec) {
				this.valueSpec = spec;
				Object configValue = spec.getValue();

				this.validatedButton = new ValidationStatusButton(0, 0, button -> {
					this.editBox.setValue("");
					if (this.valueSpec.getName().equals("seed")) {
						this.editBox.setValue(String.valueOf(rand.nextInt(Integer.MAX_VALUE)));
					} else {
						this.editBox.insertText(this.valueSpec.getDefaultValue().toString());
					}
					this.editBox.setFocus(false);
					ConfigStructureScreen.this.updateValid();
				});

				this.editBox = new TextFieldWidget(minecraft.font, 0, 0, 100, itemHeight - PADDING,
						new StringTextComponent(""));
				this.editBox.setTextColor(16777215);
				this.editBox.insertText(configValue.toString());
				this.editBox.setMaxLength(256);
				this.editBox.setCanLoseFocus(true);
				this.editBox.setFilter(this::validateTextFieldInput);

				this.children = ImmutableList.of(this.validatedButton, this.editBox);

				this.tooltipText = null;
			}

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {
				this.validatedButton.x = getScrollbarPosition() - this.validatedButton.getWidth() - 2 * PADDING;
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

				if (this.editBox != null) {
					this.editBox.x = left + (width / 2) + PADDING;
					this.editBox.y = top;
					this.editBox.setWidth((width / 2) - this.validatedButton.getWidth() - 4 * PADDING - 6);
					this.editBox.render(ms, mouseX, mouseY, partialTicks);
				}

				// Getting translations during rendering is not exactly a smart thing to do, but
				// it's just the config UI so .. meh.
				String translationKey = "gui.shrines.config." + valueSpec.getName();
				String description = I18n.get(translationKey);
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
					String i18nTooltipKey = translationKey + I18N_TOOLTIP_SUFFIX;
					this.tooltipText = (I18n.exists(i18nTooltipKey)) ? I18n.get(i18nTooltipKey) : "";
				} else if ((mouseX >= this.validatedButton.x)
						&& (mouseX < (this.validatedButton.x + this.validatedButton.getWidth()))
						&& (mouseY >= this.validatedButton.y)
						&& (mouseY < (this.validatedButton.y + this.validatedButton.getHeight()))) {
					// Tooltip for the validation button.
					this.tooltipText = (this.validatedButton.isValid()) ? I18n.get(I18N_VALID) : I18n.get(I18N_INVALID);
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

			@Override
			public void commitChanges() {
				String text = this.editBox.getValue();
				this.valueSpec.fromString(text, StructureUtils.getConfigOf(configSpecs.getName(), false));
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
				boolean flag = false;
				try {
					OptionParsingResult o = this.valueSpec.fromString(text, StructureUtils.getConfigOf(configSpecs.getName(), false));
					flag = o.isSuccess();
				} catch (Throwable e) {
					this.validatedButton.setValid(flag);
					ConfigStructureScreen.this.updateValid();
					return true;
				}
				this.validatedButton.setValid(flag);
				ConfigStructureScreen.this.updateValid();
				return true;
			}

			@Override
			public IConfigOption<?> getOption() {
				return this.valueSpec;
			}

			@Override
			public boolean getIsValid() {
				return this.validatedButton.isValid();
			}
		}

		@OnlyIn(Dist.CLIENT)
		public class ScreenedOptionEntry extends OptionEntry {
			private IConfigOption<List<?>> valueSpec;
			private Button furtherScreenButton;
			private List<IGuiEventListener> children;
			private String tooltipText;

			public ScreenedOptionEntry(IConfigOption<List<?>> spec, String structure) {
				this.valueSpec = spec;

				furtherScreenButton = new Button(0, 0, 100, 20, new StringTextComponent(this.valueSpec.getName()),
						(button) -> {
							if (this.valueSpec.getValue() instanceof List<?>) {
								ConfigStructureScreen.this.minecraft.setScreen(new NormalListScreen(
										ConfigStructureScreen.this, new StringTextComponent(this.valueSpec.getName()),
										CustomStructureData.getPossibleValuesForKey(this.valueSpec.getName()),
										this.valueSpec.getValue().stream().map(o -> o.toString())
												.collect(Collectors.toList()),
										this.valueSpec.getName()));
							}
						});
				this.furtherScreenButton.active = !CustomStructureData.getPossibleValuesForKey(this.valueSpec.getName())
						.isEmpty();
				this.children = ImmutableList.of(furtherScreenButton);

				this.tooltipText = null;
			}

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {

				this.furtherScreenButton.x = left + (width / 2) + PADDING;
				this.furtherScreenButton.y = top;
				this.furtherScreenButton.setWidth((width / 2) - 16 - 4 * PADDING - 6);
				this.furtherScreenButton.render(ms, mouseX, mouseY, partialTicks);

				// Getting translations during rendering is not exactly a smart thing to do, but
				// it's just the config UI so .. meh.
				String translationKey = "gui.shrines.config." + valueSpec.getName();
				String description = I18n.get(translationKey);
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
					String i18nTooltipKey = translationKey + I18N_TOOLTIP_SUFFIX;
					this.tooltipText = (I18n.exists(i18nTooltipKey)) ? I18n.get(i18nTooltipKey) : "";
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

			}

			@Override
			public void commitChanges() {
			}

			@Override
			public void tick() {
			}

			@Override
			public String getTooltip() {
				return this.tooltipText;
			}

			@Override
			public IConfigOption<?> getOption() {
				return this.valueSpec;
			}

			@Override
			public boolean getIsValid() {
				return true;
			}
		}

		@OnlyIn(Dist.CLIENT)
		public class PiecesOptionEntry extends OptionEntry {
			private ConfigOption<List<PieceData>> valueSpec;
			private Button furtherScreenButton;
			private List<IGuiEventListener> children;
			private String tooltipText;

			public PiecesOptionEntry(ConfigOption<List<PieceData>> valueSpec, String structure) {
				this.valueSpec = valueSpec;

				furtherScreenButton = new Button(0, 0, 100, 20, new StringTextComponent(this.valueSpec.getName()),
						(button) -> {
							if (this.valueSpec.getValue() instanceof List<?>) {
								ConfigStructureScreen.this.minecraft.setScreen(new AddResourceScreen(
										ConfigStructureScreen.this, Utils.getData(structure, false)));
							}
						});
				this.furtherScreenButton.active = ConfigStructureScreen.this.minecraft.level != null;
				this.children = ImmutableList.of(furtherScreenButton);

				this.tooltipText = null;
			}

			@Override
			public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
					int mouseY, boolean isHot, float partialTicks) {

				this.furtherScreenButton.x = left + (width / 2) + PADDING;
				this.furtherScreenButton.y = top;
				this.furtherScreenButton.setWidth((width / 2) - 16 - 4 * PADDING - 6);
				this.furtherScreenButton.render(ms, mouseX, mouseY, partialTicks);

				// Getting translations during rendering is not exactly a smart thing to do, but
				// it's just the config UI so .. meh.
				String translationKey = "gui.shrines.config." + valueSpec.getName();
				String description = I18n.get(translationKey);
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
					String i18nTooltipKey = translationKey + I18N_TOOLTIP_SUFFIX;
					this.tooltipText = (I18n.exists(i18nTooltipKey)) ? I18n.get(i18nTooltipKey) : "";
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

			}

			@Override
			public void commitChanges() {
			}

			@Override
			public void tick() {
			}

			@Override
			public String getTooltip() {
				return this.tooltipText;
			}

			@Override
			public ConfigOption<?> getOption() {
				return this.valueSpec;
			}

			@Override
			public boolean getIsValid() {
				return true;
			}
		}
	}
}
