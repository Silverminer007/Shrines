package com.silverminer.shrines.commands;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.silverminer.shrines.Shrines;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class ShrinesCommand {
	public static void register(CommandDispatcher<CommandSource> p_198528_0_) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("shrines-structures")
				.requires((ctx) -> {
					return ctx.hasPermission(2);
				});
		RequiredArgumentBuilder<CommandSource, String> options = Commands
				.argument("option", StringArgumentType.string())
				.then(Commands.argument("value", StringArgumentType.string())
						.executes(ctx -> configure(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"),
								StringArgumentType.getString(ctx, "option"),
								StringArgumentType.getString(ctx, "value"))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("add").then(Commands
				.argument("structure-name", StringArgumentType.word())
				.then(Commands.argument("seed", IntegerArgumentType.integer())
						.executes(ctx -> add(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"),
								IntegerArgumentType.getInteger(ctx, "seed")))
						.then(Commands.argument("value", StringArgumentType.string()).executes(
								ctx -> add(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"),
										IntegerArgumentType.getInteger(ctx, "seed"),
										StringArgumentType.getString(ctx, "option"),
										StringArgumentType.getString(ctx, "value")))))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("remove").then(Commands
				.argument("structure-name", StringArgumentType.word())
				.executes(ctx -> remove(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"), false))
				.then(Commands.argument("fromDisk", BoolArgumentType.bool())
						.executes(ctx -> remove(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"),
								BoolArgumentType.getBool(ctx, "fromDisk"))))));

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("help").executes(ctx -> help(ctx.getSource())));

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("query").executes(ctx -> query(ctx.getSource()))
						.then(Commands.argument("structure-name", StringArgumentType.word()).executes(
								ctx -> query(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name")))));

		literalargumentbuilder = literalargumentbuilder.then(Commands.literal("configure")
				.then(Commands.argument("structure-name", StringArgumentType.word()).then(options)));
		p_198528_0_.register(literalargumentbuilder);
	}

	public static int add(CommandSource ctx, String name, int seed, String option, String value) {
		return add(ctx, name, seed) + configure(ctx, name, option, value);
	}

	public static int configure(CommandSource ctx, String name, String option, String value) {
		List<String> config = Shrines.customStructures.get(name);
		String message;
		if (config == null) {
			message = "Failed to configure structure " + name + " because it doesn't exists";
		} else {
			boolean flag = false;
			for (int i = 0; i < config.size(); i++) {
				String o = config.get(i);
				if (o.startsWith(option)) {
					config.set(i, option + ":" + value);
					flag = true;
					break;
				}
			}
			if (flag)
				message = "Succesfully configured " + name;
			else
				message = "Failed to configure  " + name + " because " + option + " is not a valid option";
		}
		try {
			ctx.getPlayerOrException().sendMessage(new StringTextComponent(message),
					ctx.getPlayerOrException().getUUID());
		} catch (CommandSyntaxException e) {
		}
		return 0;
	}

	public static int help(CommandSource ctx) {
		try {
			ctx.getPlayerOrException().sendMessage(
					new StringTextComponent(
							"Here will come all possible config options \n restart your game after change"),
					ctx.getPlayerOrException().getUUID());
		} catch (CommandSyntaxException e) {
		}
		return 0;
	}

	public static int query(CommandSource ctx) {
		String output = "Actually there are " + Shrines.customStructures.size() + " custom structures [";
		for (int i = 0; i < Shrines.customStructures.size(); i++) {
			output = output + "\n\"" + (Shrines.customStructures.keySet().toArray())[i] + "\"";
		}
		output = output + "\n]";
		try {
			ctx.getPlayerOrException().sendMessage(new StringTextComponent(output),
					ctx.getPlayerOrException().getUUID());
		} catch (CommandSyntaxException e) {
		}
		return 0;
	}

	public static int query(CommandSource ctx, String name) {
		String output = "Config of " + name + " [";
		List<String> options = Shrines.customStructures.get(name);
		if (options == null) {
			output = "Failed to query config of " + name + " because no such structure exists";
		} else {
			for (int i = 0; i < options.size(); i++) {
				String line = options.get(i);
				if (!line.startsWith("#"))
					output = output + "\n\"" + options.get(i) + "\"";
			}
			output = output + "\n]";
		}
		try {
			ctx.getPlayerOrException().sendMessage(new StringTextComponent(output),
					ctx.getPlayerOrException().getUUID());
		} catch (CommandSyntaxException e) {
		}
		return 0;
	}

	public static int add(CommandSource ctx, String name, int seed) {
		Shrines.customStructures.put(name, Shrines.getDefaultConfig(name, seed));
		try {
			ctx.getPlayerOrException().sendMessage(new StringTextComponent(
					"Successfully added new structure named: " + name + "\nUse help option for what to do next"),
					ctx.getPlayerOrException().getUUID());
		} catch (CommandSyntaxException e) {
		}
		return 0;
	}

	public static int remove(CommandSource ctx, String name, boolean fromDisk) {
		String message;
		if (Shrines.customStructures.remove(name) == null) {
			message = "Failed to remove " + name + " because there was no custom structure with this name";
		} else {
			message = "Succesfully removed custom structure " + name + "\nRestart your game to make the changes aktive";
			if (fromDisk) {
				Shrines.customsToDelete.add(name);
			}
		}
		try {
			ctx.getPlayerOrException().sendMessage(new StringTextComponent(message),
					ctx.getPlayerOrException().getUUID());
		} catch (CommandSyntaxException e) {
		}
		return 0;
	}
}