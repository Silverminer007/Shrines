/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.commands.arguments;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.network.PacketBuffer;

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
      List<String> biomes = ShrinesMod.getInstance().getFunctionProvider().getBiomes();
      CustomStructureData data = Utils.getData(ctx.getArgument("structure-name", String.class), false);
      if (data != null)
         biomes.removeIf(biome -> newBiome ? data.blacklist.getValue().contains(biome)
               : !data.blacklist.getValue().contains(biome.toString()));
      return biomes;
   }

   public static class Serializer implements IArgumentSerializer<BiomeCSArgumentType> {
      public void serializeToNetwork(BiomeCSArgumentType args, PacketBuffer pkt) {
         pkt.writeBoolean(args.newBiome);
      }

      public BiomeCSArgumentType deserializeFromNetwork(PacketBuffer pkt) {
         return BiomeCSArgumentType.biome(pkt.readBoolean());
      }

      @Override
      public void serializeToJson(BiomeCSArgumentType args, JsonObject json) {
         json.addProperty("newBiome", args.newBiome);
      }
   }
}