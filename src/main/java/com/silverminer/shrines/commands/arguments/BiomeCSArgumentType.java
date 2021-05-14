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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeCSArgumentType extends ResourceLocationArgument {
	private final boolean newBiome;

	protected BiomeCSArgumentType(boolean newBiome) {
		this.newBiome = newBiome;
	}

	public static BiomeCSArgumentType biome(boolean newBiome) {
		return new BiomeCSArgumentType(newBiome);
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> cct, SuggestionsBuilder sb) {
		return cct.getSource() instanceof ISuggestionProvider
				? ISuggestionProvider.suggest(getValidBiomes(cct, this.newBiome), sb)
				: Suggestions.empty();
	}

	public static List<String> getValidBiomes(CommandContext<?> ctx, boolean newBiome) {
		List<ResourceLocation> biomes = ForgeRegistries.BIOMES.getKeys().stream().collect(Collectors.toList());
		CustomStructureData data = Utils.getData(ctx.getArgument("structure-name", String.class));
		if (data != null)
			biomes.removeIf(biome -> newBiome ? data.blacklist.getValue().contains(biome.toString())
					: !data.blacklist.getValue().contains(biome.toString()));
		return biomes.stream().map(ResourceLocation::toString).collect(Collectors.toList());
	}
}