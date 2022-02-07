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

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class NameCSArgumentType implements ArgumentType<String> {
    private final boolean newName;

    protected NameCSArgumentType(boolean newName) {
        this.newName = newName;
    }

    public static NameCSArgumentType oldName() {
        return new NameCSArgumentType(false);
    }

    public static NameCSArgumentType newName() {
        return new NameCSArgumentType(true);
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

    public static boolean isAllowedChar(char ch) {
        return ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '_' || ch == ':' || ch == '/' || ch == '.'
                || ch == '-';
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
        if (!this.newName) {
            return cct.getSource() instanceof ISuggestionProvider
                    ? ISuggestionProvider.suggest(Utils.getStructures(false).stream().map(CustomStructureData::getName),
                    sb)
                    : Suggestions.empty();
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public Collection<String> getExamples() {
        List<String> s = Lists.newArrayList();
        Utils.getStructures(true).forEach(csd -> s.add(csd.name));
        return s;
    }

    public static class Serializer implements IArgumentSerializer<NameCSArgumentType> {
        public void serializeToNetwork(NameCSArgumentType args, PacketBuffer buf) {
            buf.writeBoolean(args.newName);
        }

        public NameCSArgumentType deserializeFromNetwork(PacketBuffer buf) {
            if (buf.readBoolean())
                return NameCSArgumentType.newName();
            else
                return NameCSArgumentType.oldName();
        }

        @Override
        public void serializeToJson(NameCSArgumentType args, JsonObject json) {
            json.addProperty("newName", args.newName);
        }
    }
}