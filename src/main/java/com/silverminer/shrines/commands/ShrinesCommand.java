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
import com.mojang.brigadier.tree.LiteralCommandNode;
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

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("shrines-structures")
				.requires((ctx) -> {
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

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("add-piece").then(Commands
				.argument("structure-name", NameCSArgumentType.name())
				.then(Commands.argument("firstCorner", BlockPosArgument.blockPos()).then(Commands
						.argument("secondCorner", BlockPosArgument.blockPos())
						.executes(ctx -> addPiece(ctx.getSource(), NameCSArgumentType.getName(ctx, "structure-name"),
								BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
								BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), false))
						.then(Commands.literal("instantadd")
								.executes(ctx -> addPiece(ctx.getSource(),
										NameCSArgumentType.getName(ctx, "structure-name"),
										BlockPosArgument.getLoadedBlockPos(ctx, "firstCorner"),
										BlockPosArgument.getLoadedBlockPos(ctx, "secondCorner"), true)))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("configure").then(options));

		LiteralCommandNode<CommandSource> commandNode = dispatcher.register(literalargumentbuilder);
		LOGGER.info("Command node: {}", commandNode);
		dispatcher.register((Commands.literal("sh-st").requires(ctx -> ctx.hasPermission(2))
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
		}
		ctx.sendSuccess(message, false);
		return ret;
	}

	public static int remove(CommandSource ctx, String name, boolean fromDisk) {
		ITextComponent message;
		if (!Utils.customsStructs.removeIf(csd -> csd.getName().equals(name))) {
			message = new TranslationTextComponent("commands.shrines.remove.failed", name);
		} else {
			message = new TranslationTextComponent("commands.shrines.remove.success", name);
			if (fromDisk) {
				Utils.customsToDelete.add(name);
			}
		}
		ctx.sendSuccess(message, false);
		return 0;
	}

	public static int configure(CommandSource ctx, String name, String option, Object value)
			throws CommandSyntaxException {
		CustomStructureData data = Utils.getData(name);
		ITextComponent message;
		if (data == null) {
			message = new TranslationTextComponent("commands.shrines.configure.failed.structure", name);
		} else {
			OptionParsingResult res = data.fromString(option, value.toString());
			if (res.isSuccess())
				message = new TranslationTextComponent("commands.shrines.configure.success", name);
			else {
				if (res.getMessage() == null)
					message = new TranslationTextComponent("commands.shrines.configure.failed.option", name, option);
				else
					message = res.getMessage();
			}
		}
		ctx.sendSuccess(message, false);
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
		ITextComponent output;
		CustomStructureData data = Utils.getData(name);
		if (data == null) {
			output = new TranslationTextComponent("commands.shrines.query.option.failed", name);
		} else {
			output = new TranslationTextComponent("commands.shrines.query.option.success", name,
					data.toStringReadAble());
		}
		ctx.sendSuccess(output, false);
		return 0;
	}

	public static int addPiece(CommandSource ctx, String name, BlockPos pos1, BlockPos pos2, boolean instadd) {
		return 0;
	}
}