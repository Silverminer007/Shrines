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
package com.silverminer.shrines.commands.arguments;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.TranslationTextComponent;

public class OptionCSArgumentType implements ArgumentType<String> {

	protected OptionCSArgumentType() {
	}

	public static OptionCSArgumentType option() {
		return new OptionCSArgumentType();
	}

	public static String getOption(final CommandContext<CommandSource> context, final String name)
			throws CommandSyntaxException {
		String str = context.getArgument(name, String.class);
		String s = str.toLowerCase(Locale.ROOT);
		if (!s.equals(str))
			context.getSource().sendFailure(new TranslationTextComponent("commands.shrines.failure.lower_case",
					context.getArgument("structure-name", String.class)));
		if (CustomStructureData.OPTIONS.stream().anyMatch(n -> n.equals(s))) {
			return s;
		} else {
			throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.shrines.failure.option", s))
					.create();
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> cct, SuggestionsBuilder sb) {
		return cct.getSource() instanceof ISuggestionProvider
				? ISuggestionProvider.suggest(CustomStructureData.OPTIONS.stream(), sb)
				: Suggestions.empty();
	}

	@Override
	public String parse(final StringReader reader) throws CommandSyntaxException {
		int i = reader.getCursor();

		while (reader.canRead() && isAllowedChar(reader.peek())) {
			reader.skip();
		}

		String s = reader.getString().substring(i, reader.getCursor());
		return s;
	}

	public static boolean isAllowedChar(char ch) {
		return ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '_' || ch == ':' || ch == '/' || ch == '.'
				|| ch == '-';
	}

	@Override
	public Collection<String> getExamples() {
		return CustomStructureData.OPTIONS;
	}
}