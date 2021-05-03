package com.silverminer.shrines.commands;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.StringTextComponent;

public class NewNameCSArgumentType implements ArgumentType<String> {

	private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(
			new StringTextComponent("Name Argument was invalid"));
	private static final List<String> EXAMPLES = Lists.newArrayList("example", "custom", "structure", "insert_the_new_name");

	protected NewNameCSArgumentType() {
	}

	public static NewNameCSArgumentType name() {
		return new NewNameCSArgumentType();
	}

	public static String getName(final CommandContext<?> context, final String name) throws CommandSyntaxException {
		String s = context.getArgument(name, String.class);
		if (!Utils.customsStructs.stream().map(CustomStructureData::getName).anyMatch(n -> n.equals(s))) {
			return s;
		} else {
			throw new SimpleCommandExceptionType(
					new StringTextComponent("Name Argument was invalid, there is already such an structure")).create();
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> cct, SuggestionsBuilder sb) {
		List<String> l = EXAMPLES;
		l.removeAll(Utils.customsStructs.stream().map(CustomStructureData::getName).collect(Collectors.toList()));
		return cct.getSource() instanceof ISuggestionProvider ? ISuggestionProvider.suggest(l, sb)
				: Suggestions.empty();
	}

	@Override
	public String parse(final StringReader reader) throws CommandSyntaxException {
		int i = reader.getCursor();

		while (reader.canRead() && isAllowedChar(reader.peek())) {
			reader.skip();
		}

		String s = reader.getString().substring(i, reader.getCursor());

		try {
			return s;
		} catch (ResourceLocationException resourcelocationexception) {
			reader.setCursor(i);
			throw ERROR_INVALID.createWithContext(reader);
		}
	}

	public static boolean isAllowedChar(char ch) {
		return ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '_' || ch == ':' || ch == '/' || ch == '.'
				|| ch == '-';
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}