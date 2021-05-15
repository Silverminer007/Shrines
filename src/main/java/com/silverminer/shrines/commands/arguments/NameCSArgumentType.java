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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class NameCSArgumentType implements ArgumentType<String> {

	protected NameCSArgumentType() {
	}

	public static NameCSArgumentType name() {
		return new NameCSArgumentType();
	}

	public static String getName(final CommandContext<CommandSource> context, final String name)
			throws CommandSyntaxException {
		String str = context.getArgument(name, String.class);
		String s = str.toLowerCase(Locale.ROOT);
		if (!s.equals(str))
			context.getSource()
					.sendFailure(new TranslationTextComponent("commands.shrines.failure.lower_case", "structure-name"));
		return s;
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
		return s;
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

	public static class Serializer implements IArgumentSerializer<NameCSArgumentType> {
		public void serializeToNetwork(NameCSArgumentType p_197072_1_, PacketBuffer p_197072_2_) {
		}

		public NameCSArgumentType deserializeFromNetwork(PacketBuffer p_197071_1_) {
			return NameCSArgumentType.name();
		}

		@Override
		public void serializeToJson(NameCSArgumentType p_212244_1_, JsonObject p_212244_2_) {
		}
	}
}