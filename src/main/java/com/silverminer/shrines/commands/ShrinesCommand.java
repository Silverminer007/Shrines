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
package com.silverminer.shrines.commands;

import java.util.Random;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.silverminer.shrines.commands.arguments.BiomeCSArgumentType;
import com.silverminer.shrines.commands.arguments.BiomeCategoryCSArgumentType;
import com.silverminer.shrines.commands.arguments.NameCSArgumentType;
import com.silverminer.shrines.commands.arguments.OptionCSArgumentType;
import com.silverminer.shrines.structures.custom.helper.ConfigOption;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.OptionParsingResult;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.biome.Biome.Category;

public class ShrinesCommand {
	protected static final Logger LOGGER = LogManager.getLogger(ShrinesCommand.class);

	/**
	 * 
	 * @param dispatcher
	 */
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("sh-st").requires((ctx) -> {
			return ctx.hasPermission(2);
		});

		RequiredArgumentBuilder<CommandSource, String> options = Commands.argument("structure-name",
				NameCSArgumentType.oldName());
		for (ConfigOption<?> co : new CustomStructureData("dummy", 0).CONFIGS) {
			if (!co.getUseInCommand()) {
				continue;
			}
			options = options.then(Commands.literal(co.getName())
					.then(Commands.argument("value", co.getArgument()).executes(
							ctx -> configure(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
									co.getName(), co.getCommandValue(ctx, co.getName())))));
		}

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("add")
						.then(Commands.argument("structure-name", NameCSArgumentType.newName())
								.executes(ctx -> add(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
										new Random().nextInt(Integer.MAX_VALUE)))
								.then(Commands.argument("seed", IntegerArgumentType.integer())
										.executes(ctx -> add(ctx.getSource(),
												NameCSArgumentType.getName(ctx, "structure-name"),
												IntegerArgumentType.getInteger(ctx, "seed"))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("remove").then(Commands
				.argument("structure-name", NameCSArgumentType.oldName())
				.executes(ctx -> remove(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"), false))
				.then(Commands.argument("fromDisk", BoolArgumentType.bool())
						.executes(ctx -> remove(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BoolArgumentType.getBool(ctx, "fromDisk"))))));

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("help").executes(ctx -> help(ctx.getSource())));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("query")
				.executes(ctx -> query(ctx.getSource()))
				.then(Commands.argument("structure-name", NameCSArgumentType.oldName())
						.executes(ctx -> query(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name")))
						.then(Commands.argument("option", OptionCSArgumentType.option()).executes(
								ctx -> query(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
										OptionCSArgumentType.getOption(ctx, "option"))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("save-resource").then(Commands
				.argument("structure-name", NameCSArgumentType.oldName())
				.then(Commands.argument("firstCorner", BlockPosArgument.blockPos())
						.then(Commands.argument("secondCorner", BlockPosArgument.blockPos())
								.then(Commands.argument("include-entities", BoolArgumentType.bool())
										.executes(ctx -> addPieces(ctx.getSource(),
												NameCSArgumentType.getName(ctx, "structure-name"),
												BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
												BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), false,
												BoolArgumentType.getBool(ctx, "include-entities")))
										.then(Commands.literal("instantadd")
												.executes(ctx -> addPieces(ctx.getSource(),
														NameCSArgumentType.getName(ctx, "structure-name"),
														BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
														BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), true,
														BoolArgumentType.getBool(ctx, "include-entities")))))
								.executes(
										ctx -> addPieces(
												ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
												BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
												BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), false, false))
								.then(Commands.literal("instantadd").executes(ctx -> addPieces(ctx.getSource(),
										NameCSArgumentType.getName(ctx, "structure-name"),
										BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
										BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), true, false)))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("configure").then(options));

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("save").executes(ctx -> save(ctx.getSource())));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("load-resource").then(Commands
				.argument("structure-name", NameCSArgumentType.oldName())
				.then(Commands.argument("position", BlockPosArgument.blockPos())
						.executes(ctx -> load(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BlockPosArgument.getLoadedBlockPos(ctx, "position"), Rotation.NONE))
						.then(Commands.literal("0").executes(
								ctx -> load(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
										BlockPosArgument.getLoadedBlockPos(ctx, "position"), Rotation.NONE)))
						.then(Commands.literal("90").executes(
								ctx -> load(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
										BlockPosArgument.getLoadedBlockPos(ctx, "position"), Rotation.CLOCKWISE_90)))
						.then(Commands.literal("180").executes(
								ctx -> load(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
										BlockPosArgument.getLoadedBlockPos(ctx, "position"), Rotation.CLOCKWISE_180)))
						.then(Commands.literal("270").executes(ctx -> load(ctx.getSource(),
								NameCSArgumentType.getName(ctx, "structure-name"),
								BlockPosArgument.getLoadedBlockPos(ctx, "position"), Rotation.COUNTERCLOCKWISE_90))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("blacklist").then(Commands
				.argument("structure-name", NameCSArgumentType.oldName())
				.then(Commands.literal("add").then(Commands.argument("biome", BiomeCSArgumentType.biome(true))
						.executes(ctx -> blacklist(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BiomeListAction.ADD, ctx.getArgument("biome", ResourceLocation.class)))))
				.then(Commands.literal("remove").then(Commands.argument("biome", BiomeCSArgumentType.biome(false))
						.executes(ctx -> blacklist(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BiomeListAction.REMOVE, ctx.getArgument("biome", ResourceLocation.class)))))
				.then(Commands.literal("query").executes(ctx -> blacklist(ctx.getSource(),
						NameCSArgumentType.getName(ctx, "structure-name"), BiomeListAction.QUERY, null)))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("dimensions").then(Commands
				.argument("structure-name", NameCSArgumentType.oldName())
				.then(Commands.literal("add").then(Commands.argument("dimension", DimensionArgument.dimension())
						.executes(ctx -> dimensions(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BiomeListAction.ADD, DimensionArgument.getDimension(ctx, "dimension").dimension().location().toString()))))
				.then(Commands.literal("remove").then(Commands.argument("dimension", DimensionArgument.dimension())
						.executes(ctx -> dimensions(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BiomeListAction.REMOVE, DimensionArgument.getDimension(ctx, "dimension").dimension().location().toString()))))
				.then(Commands.literal("query").executes(ctx -> blacklist(ctx.getSource(),
						NameCSArgumentType.getName(ctx, "structure-name"), BiomeListAction.QUERY, null)))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("whitelist").then(Commands
				.argument("structure-name", NameCSArgumentType.oldName())
				.then(Commands.literal("add").then(Commands
						.argument("biome", BiomeCategoryCSArgumentType.category(true))
						.executes(ctx -> whitelist(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BiomeListAction.ADD, BiomeCategoryCSArgumentType.getCategory(ctx, "biome")))))
				.then(Commands.literal("remove").then(Commands
						.argument("biome", BiomeCategoryCSArgumentType.category(false))
						.executes(ctx -> whitelist(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BiomeListAction.REMOVE, BiomeCategoryCSArgumentType.getCategory(ctx, "biome")))))
				.then(Commands.literal("query").executes(ctx -> whitelist(ctx.getSource(),
						NameCSArgumentType.getName(ctx, "structure-name"), BiomeListAction.QUERY, null)))));

		dispatcher.register(literalargumentbuilder);
		dispatcher.register((Commands.literal("shrines-structures").requires(ctx -> ctx.hasPermission(2))
				.redirect(literalargumentbuilder.build())));
	}

	public static int help(CommandSource ctx) throws CommandSyntaxException {
		ITextComponent help = new TranslationTextComponent("commands.shrines.help.help").withStyle((style) -> {
			return style.withColor(TextFormatting.GREEN)
					.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
							"https://github.com/Silverminer007/Shrines/wiki/Adding-Custom-Structures"))
					.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new TranslationTextComponent("commands.shrines.help.tooltip")));
		});
		ctx.sendSuccess(new TranslationTextComponent("commands.shrines.help", help), false);
		return 0;
	}

	public static int add(CommandSource ctx, String name, int seed) throws CommandSyntaxException {
		int ret = 0;
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(name, true);
		if (data != null) {
			message = new TranslationTextComponent("commands.shrines.add.failed", name);
			ret = -1;
		} else {
			Utils.customsStructs.add(new CustomStructureData(name, seed));
			ITextComponent conf = new TranslationTextComponent("commands.shrines.add.success.help")
					.withStyle((style) -> {
						return style.withColor(TextFormatting.GREEN)
								.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
										"/shrines-structures configure " + name + " "))
								.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new TranslationTextComponent("commands.shrines.add.success.help.tooltip")));
					});
			message = new TranslationTextComponent("commands.shrines.add.success", name, conf);
			success = true;
			CustomStructureData.sendToClients();
		}
		if (success)
			ctx.sendSuccess(message, false);
		else
			ctx.sendFailure(message);
		return ret;
	}

	public static int remove(CommandSource ctx, String name, boolean fromDisk) {
		ITextComponent message;
		boolean success = false;
		if (!Utils.customsStructs.removeIf(csd -> csd.getName().equals(name))) {
			message = new TranslationTextComponent("commands.shrines.remove.failed", name);
		} else {
			message = new TranslationTextComponent("commands.shrines.remove.success", name);
			if (fromDisk) {
				Utils.customsToDelete.add(name);
			}
			success = true;
		}
		CustomStructureData.sendToClients();
		if (success)
			ctx.sendSuccess(message, false);
		else
			ctx.sendFailure(message);
		return 0;
	}

	public static int configure(CommandSource ctx, String name, String option, Object value)
			throws CommandSyntaxException {
		CustomStructureData data = Utils.getData(name, true);
		ITextComponent message;
		boolean success = false;
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.configure.failed.structure", name);
		} else {
			OptionParsingResult res = data.fromString(option, value.toString());
			if (res.isSuccess()) {
				message = new TranslationTextComponent("commands.shrines.configure.success", name);
				success = true;
			} else {
				if (res.getMessage() == null)
					message = new TranslationTextComponent("commands.shrines.configure.failed.option", name, option);
				else
					message = res.getMessage();
			}
			CustomStructureData.sendToClients();
		}
		if (success)
			ctx.sendSuccess(message, false);
		else
			ctx.sendFailure(message);
		return 0;
	}

	public static int query(CommandSource ctx) throws CommandSyntaxException {
		String output = "";
		for (int i = 0; i < Utils.customsStructs.size(); i++) {
			output = output + "\n\"" + Utils.customsStructs.get(i).getName() + "\"";
		}
		ctx.sendSuccess(
				new TranslationTextComponent("commands.shrines.query.structure", Utils.customsStructs.size(), output),
				false);
		return 0;
	}

	public static int query(CommandSource ctx, String name) throws CommandSyntaxException {
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(name, true);
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.query.option.failed", name);
		} else {
			message = new TranslationTextComponent("commands.shrines.query.option.success", name,
					data.toStringReadAble());
			success = true;
		}
		if (success)
			ctx.sendSuccess(message, false);
		else
			ctx.sendFailure(message);
		return 0;
	}

	public static int query(CommandSource ctx, String name, String option) throws CommandSyntaxException {
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(name, true);
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.query.option.failed", name);
		} else {
			ConfigOption<?> configOption = null;
			for (ConfigOption<?> co : data.CONFIGS) {
				if (co.getName().equals(option)) {
					configOption = co;
				}
			}
			if (configOption != null) {
				message = new TranslationTextComponent("commands.shrines.query.option.value.success", name, option,
						configOption.getValue());
				success = true;
			} else {
				message = new TranslationTextComponent("commands.shrines.query.option.value.failed", option, name);
			}
		}
		if (success)
			ctx.sendSuccess(message, false);
		else
			ctx.sendFailure(message);
		return 0;
	}

	public static int addPieces(CommandSource ctx, String name, BlockPos pos1, BlockPos pos2, boolean instadd,
			boolean includeEntities) {
		ITextComponent message;
		boolean success = false;
		if (ctx.getLevel() == null) {
			message = new TranslationTextComponent("commands.shrines.resource.failed.world_not_present");
		} else {
			CustomStructureData data = Utils.getData(name, true);
			if (data == null) {
				message = new TranslationTextComponent("commands.shrines.failed.structure", name);
			} else {
				if (data.calculateBounds(pos1, pos2, ctx.getLevel().dimension())) {
					if (instadd) {
						String author;
						try {
							author = ctx.getPlayerOrException().getName().getString();
						} catch (CommandSyntaxException e) {
							author = "admin";
						}
						if (!data.savePieces(ctx.getLevel(), ctx.getServer(), author, includeEntities)) {
							message = new TranslationTextComponent("commands.shrines.resource.failed.save", name);
						} else {
							data.addBounds();
							message = new TranslationTextComponent("commands.shrines.resource.success.added", name);
							success = true;
						}
					} else {
						ITextComponent yes = new TranslationTextComponent(
								"commands.shrines.resource.success.prepared.yes").withStyle((style) -> {
									return style.withColor(TextFormatting.GREEN)
											.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
													"/shrines-structures save-resource " + name + " " + pos1.getX()
															+ " " + pos1.getY() + " " + pos1.getZ() + " " + pos2.getX()
															+ " " + pos2.getY() + " " + pos2.getZ() + " "
															+ includeEntities + " instantadd"))
											.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
													new TranslationTextComponent(
															"commands.shrines.resource.success.prepared.yes.help")));
								});
						ITextComponent configure = new TranslationTextComponent(
								"commands.shrines.resource.success.prepared.configure").withStyle((style) -> {
									return style.withColor(TextFormatting.YELLOW)
											.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
													"/shrines-structures save-resource " + name + " " + pos1.getX() + " "
															+ pos1.getY() + " " + pos1.getZ() + " " + pos2.getX() + " "
															+ pos2.getY() + " " + pos2.getZ() + " " + includeEntities))
											.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
													new TranslationTextComponent(
															"commands.shrines.resource.success.prepared.configure.help")));
								});
						message = new TranslationTextComponent("commands.shrines.resource.success.prepared", name, yes,
								configure);
						success = true;
					}
					CustomStructureData.sendToClients();
				} else {
					message = new TranslationTextComponent("commands.shrines.resource.failed.calculate", name);
				}
			}
		}
		if (success) {
			ctx.sendSuccess(message, false);
		} else {
			ctx.sendFailure(message);
		}
		return 0;
	}

	public static int save(CommandSource ctx) {
		Utils.saveStructures();
		ITextComponent message = new TranslationTextComponent("commands.shrines.saved");
		ctx.sendSuccess(message, false);
		return 0;
	}

	public static int load(CommandSource ctx, String structure, BlockPos position, Rotation rotation) {
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(structure, true);
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.failed.structure", structure);
		} else {
			if (data.loadPieces(ctx.getLevel(), ctx.getServer(), position, rotation)) {
				message = new TranslationTextComponent("commands.shrines.load.success", structure, position);
				success = true;
			} else {
				message = new TranslationTextComponent("commands.shrines.load.failed", structure, position);
			}
		}
		if (success) {
			ctx.sendSuccess(message, true);
		} else {
			ctx.sendFailure(message);
		}
		return 0;
	}

	public static int blacklist(CommandSource ctx, String structure, BiomeListAction action,
			@Nullable ResourceLocation biome) {
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(structure, true);
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.failed.structure", structure);
		} else {
			switch (action) {
			case ADD:
				if (biome == null) {
					message = new TranslationTextComponent("commands.shrines.biomelist.failed.invalid_biome");
				} else {
					if (!data.blacklist.getValue().contains(biome.toString())) {
						data.blacklist.getValue().add(biome.toString());
						message = new TranslationTextComponent("commands.shrines.blacklist.success.add", biome);
						success = true;
					} else {
						message = new TranslationTextComponent("commands.shrines.blacklist.failed.add", biome);
					}
				}
				break;
			case REMOVE:
				if (biome == null) {
					message = new TranslationTextComponent("commands.shrines.blacklist.failed.invalid_biome");
				} else {
					if (data.blacklist.getValue().remove(biome.toString())) {
						message = new TranslationTextComponent("commands.shrines.blacklist.success.remove", biome);
						success = true;
					} else {
						message = new TranslationTextComponent("commands.shrines.blacklist.failed.remove", biome);
					}
				}
				break;
			case QUERY:
				message = new TranslationTextComponent("commands.shrines.blacklist.success.query", structure,
						data.blacklist.getValue());
				success = true;
				break;
			default:
				message = new TranslationTextComponent("commands.shrines.biomelist.failed.noaction");
			}
			CustomStructureData.sendToClients();
		}
		if (success) {
			ctx.sendSuccess(message, true);
		} else {
			ctx.sendFailure(message);
		}
		return 0;
	}

	// TODO 1.8.1 Add translations (English/German/Spanish)
	public static int dimensions(CommandSource ctx, String structure, BiomeListAction action,
			@Nullable String dimension) {
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(structure, true);
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.failed.structure", structure);
		} else {
			switch (action) {
			case ADD:
				if (dimension == null) {
					message = new TranslationTextComponent("commands.shrines.biomelist.failed.invalid_biome");
				} else {
					if (!data.dimensions.getValue().contains(dimension.toString())) {
						data.dimensions.getValue().add(dimension.toString());
						message = new TranslationTextComponent("commands.shrines.dimension.success.add", dimension);
						success = true;
					} else {
						message = new TranslationTextComponent("commands.shrines.dimension.failed.add", dimension);
					}
				}
				break;
			case REMOVE:
				if (dimension == null) {
					message = new TranslationTextComponent("commands.shrines.dimension.failed.invalid_biome");
				} else {
					if (data.dimensions.getValue().remove(dimension.toString())) {
						message = new TranslationTextComponent("commands.shrines.dimension.success.remove", dimension);
						success = true;
					} else {
						message = new TranslationTextComponent("commands.shrines.dimension.failed.remove", dimension);
					}
				}
				break;
			case QUERY:
				message = new TranslationTextComponent("commands.shrines.dimension.success.query", structure,
						data.dimensions.getValue());
				success = true;
				break;
			default:
				message = new TranslationTextComponent("commands.shrines.biomelist.failed.noaction");
			}
			CustomStructureData.sendToClients();
		}
		if (success) {
			ctx.sendSuccess(message, true);
		} else {
			ctx.sendFailure(message);
		}
		return 0;
	}

	public static int whitelist(CommandSource ctx, String structure, BiomeListAction action, Category category) {
		ITextComponent message;
		boolean success = false;
		CustomStructureData data = Utils.getData(structure, true);
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.failed.structure", structure);
		} else {
			switch (action) {
			case ADD:
				if (category == null) {
					message = new TranslationTextComponent("commands.shrines.biomelist.failed.invalid_biome");
				} else {
					if (!data.categories.getValue().contains(category)) {
						data.categories.getValue().add(category);
						message = new TranslationTextComponent("commands.shrines.whitelist.success.add", category);
						success = true;
					} else {
						message = new TranslationTextComponent("commands.shrines.whitelist.failed.add", category);
					}
				}
				break;
			case REMOVE:
				if (category == null) {
					message = new TranslationTextComponent("commands.shrines.whitelist.failed.invalid_biome");
				} else {
					if (data.categories.getValue().remove(category)) {
						message = new TranslationTextComponent("commands.shrines.whitelist.success.remove", category);
						success = true;
					} else {
						message = new TranslationTextComponent("commands.shrines.whitelist.failed.remove", category);
					}
				}
				break;
			case QUERY:
				message = new TranslationTextComponent("commands.shrines.whitelist.success.query", structure,
						data.categories.getValue());
				success = true;
				break;
			default:
				message = new TranslationTextComponent("commands.shrines.biomelist.failed.noaction");
			}
			CustomStructureData.sendToClients();
		}
		if (success) {
			ctx.sendSuccess(message, true);
		} else {
			ctx.sendFailure(message);
		}
		return 0;
	}

	public static enum BiomeListAction {
		ADD, REMOVE, QUERY;
	}
}