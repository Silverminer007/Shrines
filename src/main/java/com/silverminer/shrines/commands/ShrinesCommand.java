package com.silverminer.shrines.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ShrinesCommand {
	public static void register(CommandDispatcher<CommandSource> p_198528_0_) {
		LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("shrines-structures")
				.requires((ctx) -> {
					return ctx.hasPermission(2);
				});

		literalargumentbuilder = literalargumentbuilder
				.then(Commands.literal("add").then(Commands.argument("structure-name", StringArgumentType.word()))
						.then(Commands.argument("seed", IntegerArgumentType.integer()))
						.executes(ctx -> add(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"),
								IntegerArgumentType.getInteger(ctx, "seed"), ""))
						.then(Commands.argument("options", StringArgumentType.string()))
						.executes(ctx -> add(ctx.getSource(), StringArgumentType.getString(ctx, "structure-name"),
								IntegerArgumentType.getInteger(ctx, "seed"), StringArgumentType.getString(ctx, "options"))));

		p_198528_0_.register(literalargumentbuilder);
	}

	public static int perfomAction(CommandSource source, Type t) {

		return 0;
	}

	public static int add(CommandSource ctx, String name, int seed, String... options) {
		return 0;
	}

	public static enum Type {
		ADD, REMOVE, QUERY, EDIT;
	}
}