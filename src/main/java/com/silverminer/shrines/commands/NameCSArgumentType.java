package com.silverminer.shrines.commands;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

public class NameCSArgumentType implements ArgumentType<String> {

	private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(
			new StringTextComponent("Name Argument was invalid"));

	protected NameCSArgumentType() {
	}

	public static NameCSArgumentType name() {
		return new NameCSArgumentType();
	}

	public static String getName(final CommandContext<?> context, final String name) throws CommandSyntaxException {
		String s = context.getArgument(name, String.class);
		if (Utils.customsStructs.stream().map(CustomStructureData::getName).anyMatch(n -> n.equals(s))) {
			return s;
		} else {
			throw new SimpleCommandExceptionType(new StringTextComponent("Name Argument was invalid")).create();
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> cct, SuggestionsBuilder sb) {
		return cct.getSource() instanceof ISuggestionProvider
				? ISuggestionProvider.suggest(Utils.customsStructs.stream().map(CustomStructureData::getName), sb)
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
		List<String> s = Lists.newArrayList();
		Utils.customsStructs.forEach(csd -> s.add(csd.name));
		return s;
	}
}