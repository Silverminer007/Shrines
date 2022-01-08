/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.commands.arguments;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

public class BiomeCategoryCSArgumentType implements ArgumentType<String> {
   private final boolean newCat;

   protected BiomeCategoryCSArgumentType(boolean newCat) {
      this.newCat = newCat;
   }

   public static BiomeCategoryCSArgumentType category(boolean newCat) {
      return new BiomeCategoryCSArgumentType(newCat);
   }

   public static Biome.Category getCategory(final CommandContext<CommandSource> context, final String name)
         throws CommandSyntaxException {
      String str = context.getArgument(name, String.class);
      String s = str.toUpperCase(Locale.ROOT);
      Biome.Category c = Biome.Category.valueOf(s);
      if (c != null) {
         return c;
      } else {
         throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.shrines.failure.category"))
               .create();
      }
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

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> cct, SuggestionsBuilder sb) {
      return cct.getSource() instanceof ISuggestionProvider
            ? ISuggestionProvider.suggest(getValidCategories(cct, this.newCat), sb)
            : Suggestions.empty();
   }

   public static List<String> getValidCategories(CommandContext<?> ctx, boolean newCat) {
      List<Category> cats = Lists.newArrayList(Biome.Category.values());
      CustomStructureData data = Utils.getData(ctx.getArgument("structure-name", String.class), false);
      if (data != null)
         cats.removeIf(cat -> newCat ? data.categories.getValue().contains(cat)
               : !data.categories.getValue().contains(cat));
      return cats.stream().map(Biome.Category::getName).collect(Collectors.toList());
   }

   @Override
   public Collection<String> getExamples() {
      return Lists.newArrayList(Biome.Category.values()).stream().map(Biome.Category::getName)
            .collect(Collectors.toList());
   }

   public static boolean isAllowedChar(char ch) {
      return ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '_' || ch == ':' || ch == '/' || ch == '.'
            || ch == '-';
   }

   public static class Serializer implements IArgumentSerializer<BiomeCategoryCSArgumentType> {
      public void serializeToNetwork(BiomeCategoryCSArgumentType args, PacketBuffer pkt) {
         pkt.writeBoolean(args.newCat);
      }

      public BiomeCategoryCSArgumentType deserializeFromNetwork(PacketBuffer pkt) {
         return BiomeCategoryCSArgumentType.category(pkt.readBoolean());
      }

      @Override
      public void serializeToJson(BiomeCategoryCSArgumentType args, JsonObject json) {
         json.addProperty("newCat", args.newCat);
      }
   }
}