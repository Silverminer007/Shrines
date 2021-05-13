package com.silverminer.shrines.commands;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.silverminer.shrines.structures.custom.helper.ConfigOption;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.OptionParsingResult;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class ShrinesCommand {
	protected static final Logger LOGGER = LogManager.getLogger(ShrinesCommand.class);

	/**
	 * TODO Add option to reset settings to last save state
	 * TODO add save command option
	 * TODO Fix biome blacklist gives wrong success notes
	 * TODO Add german and spanish translations
	 * @param dispatcher
	 */
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("sh-st").requires((ctx) -> {
			return ctx.hasPermission(2);
		});

		RequiredArgumentBuilder<CommandSource, String> options = Commands.argument("structure-name",
				NameCSArgumentType.name());
		for (ConfigOption<?> co : new CustomStructureData("dummy", 0).CONFIGS) {
			if (!co.getUseInCommand()) {
				continue;
			}
			options = options.then(Commands.literal(co.getName())
					.then(Commands.argument("value", co.getArgument()).executes(
							ctx -> configure(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
									co.getName(), co.getCommandValue(ctx, co.getName())))));
		}

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("add")
				.then(Commands.argument("structure-name", NewNameCSArgumentType.name())
						.executes(ctx -> add(ctx.getSource(), NewNameCSArgumentType.getName(ctx, "structure-name"),
								new Random().nextInt(Integer.MAX_VALUE)))
						.then(Commands.argument("seed", IntegerArgumentType.integer())
								.executes(ctx -> add(ctx.getSource(),
										NewNameCSArgumentType.getName(ctx, "structure-name"),
										IntegerArgumentType.getInteger(ctx, "seed"))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("remove").then(Commands
				.argument("structure-name", NameCSArgumentType.name())
				.executes(ctx -> remove(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"), false))
				.then(Commands.argument("fromDisk", BoolArgumentType.bool())
						.executes(ctx -> remove(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BoolArgumentType.getBool(ctx, "fromDisk"))))));

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("help").executes(ctx -> help(ctx.getSource())));

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("query").executes(ctx -> query(ctx.getSource()))
						.then(Commands.argument("structure-name", NameCSArgumentType.name()).executes(
								ctx -> query(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name")))));

		// TODO Add more options like include entities
		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("set-resource").then(Commands
				.argument("structure-name", NameCSArgumentType.name())
				.then(Commands.argument("firstCorner", BlockPosArgument.blockPos()).then(Commands
						.argument("secondCorner", BlockPosArgument.blockPos())
						.executes(ctx -> addPieces(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
								BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), false))
						.then(Commands.literal("instantadd")
								.executes(ctx -> addPieces(ctx.getSource(),
										NameCSArgumentType.getName(ctx, "structure-name"),
										BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
										BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), true)))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("configure").then(options));

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
		CustomStructureData data = null;
		for (CustomStructureData csd : Utils.customsStructs) {
			if (csd.getName() == name) {
				data = csd;
				break;
			}
		}
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
		if (success)
			ctx.sendSuccess(message, false);
		else
			ctx.sendFailure(message);
		return 0;
	}

	public static int configure(CommandSource ctx, String name, String option, Object value)
			throws CommandSyntaxException {
		CustomStructureData data = Utils.getData(name);
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
		CustomStructureData data = Utils.getData(name);
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

	public static int addPieces(CommandSource ctx, String name, BlockPos pos1, BlockPos pos2, boolean instadd) {
		ITextComponent message;
		boolean success = false;
		if (ctx.getLevel() == null) {
			message = new TranslationTextComponent("commands.shrines.resource.failed.world_not_present");
		} else {
			CustomStructureData data = Utils.getData(name);
			if (data == null) {
				message = new TranslationTextComponent("commands.shrines.resource.failed.structure", name);
			} else {
				if (data.calculateBounds(pos1, pos2)) {
					if (instadd) {
						String author;
						try {
							author = ctx.getPlayerOrException().getName().getString();
						} catch (CommandSyntaxException e) {
							author = "admin";
						}
						if (!data.savePieces(ctx.getLevel(), ctx.getServer(), author)) {
							message = new TranslationTextComponent("commands.shrines.resource.failed.save", name);
						} else {
							data.addBounds();
							message = new TranslationTextComponent("commands.shrines.resource.success.added", name);
							success = true;
						}
					} else {
						ITextComponent yes = new TranslationTextComponent("commands.shrines.resource.success.prepared.yes").withStyle((style) -> {
							return style.withColor(TextFormatting.GREEN)
									.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
											"/shrines-structures set-resource " + name + " " + pos1.getX() + " " + pos1.getY() + " " + pos1.getZ() + " " + pos2.getX() + " " + pos2.getY() + " " + pos2.getZ() + " instantadd"))
									.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new TranslationTextComponent("commands.shrines.resource.success.prepared.yes.help")));
						});
						ITextComponent configure = new TranslationTextComponent("commands.shrines.resource.success.prepared.configure").withStyle((style) -> {
							return style.withColor(TextFormatting.YELLOW)
									.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
											"/shrines-structures set-resource " + name + " " + pos1.getX() + " " + pos1.getY() + " " + pos1.getZ() + " " + pos2.getX() + " " + pos2.getY() + " " + pos2.getZ()))
									.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new TranslationTextComponent("commands.shrines.resource.success.prepared.configure.help")));
						});
						message = new TranslationTextComponent("commands.shrines.resource.success.prepared", name, yes, configure);
						success = true;
					}
					data.sendToClients();
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
}