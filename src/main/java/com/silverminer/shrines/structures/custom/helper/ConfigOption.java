/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.custom.helper;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.silverminer.shrines.config.IConfigOption;

import net.minecraft.command.CommandSource;

public class ConfigOption<T> implements IConfigOption<T> {
    protected static final Logger LOGGER = LogManager.getLogger(ConfigOption.class);
    private final String name;
    private final T defaultValue;
    private final boolean command;
    private T value;
    private Function<String, T> fromString;
    private Function<T, String> toString;
    private ArgumentType<?> argument;
    private BiFunction<CommandContext<CommandSource>, String, ?> argument_to_string;

    public <U> ConfigOption(String name, T value, Function<String, T> fromString, ArgumentType<U> argument,
                            BiFunction<CommandContext<CommandSource>, String, U> argument_to_string) {
        this(name, value, fromString, argument, argument_to_string, true);
    }

    public <U> ConfigOption(String name, T value, Function<String, T> fromString, ArgumentType<U> argument,
                            BiFunction<CommandContext<CommandSource>, String, U> argument_to_string, boolean command) {
        this(name, value, fromString, String::valueOf, argument, argument_to_string, command);
    }

    public <U> ConfigOption(String name, T value, Function<String, T> fromString, Function<T, String> toString,
                            ArgumentType<U> argument, BiFunction<CommandContext<CommandSource>, String, U> argument_to_string,
                            boolean command) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;
        this.fromString = fromString;
        this.argument = argument;
        this.argument_to_string = argument_to_string;
        this.command = command;
        this.toString = toString;
    }

    public boolean equals(Object o) {
        if (o instanceof IConfigOption) {
            return ((IConfigOption<?>) o).getName() == this.getName();
        } else {
            return false;
        }
    }

    public String toString() {
        return this.name + ":" + this.toString.apply(this.getValue());
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    public Function<String, T> getFromString(String option) {
        return fromString;
    }

    public void setValue(T v, String structure) {
        this.value = v;
    }

    public boolean getUseInCommand() {
        return this.command;
    }

    public ArgumentType<?> getArgument() {
        return this.argument;
    }

    public Object getCommandValue(CommandContext<CommandSource> ctx, String s) {
        try {
            Object o = this.getCommandValueFunction().apply(ctx, "value");
            return o;
        } catch (Throwable t) {
            LOGGER.error(t);
        }
        return null;
    }

    public BiFunction<CommandContext<CommandSource>, String, ?> getCommandValueFunction() {
        return this.argument_to_string;
    }
}