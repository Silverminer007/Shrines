package com.silverminer.shrines.structures.custom.helper;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;

public class ConfigOption<T> {
	protected static final Logger LOGGER = LogManager.getLogger(ConfigOption.class);
	private final String name;
	private T value;
	private Function<String, T> fromString;
	private ArgumentType<?> argument;
	private BiFunction<CommandContext<CommandSource>, String, ?> argument_to_string;

	public <U> ConfigOption(String name, T value, Function<String, T> fromString, ArgumentType<U> argument,
			BiFunction<CommandContext<CommandSource>, String, U> argument_to_string) {
		this.name = name;
		this.value = value;
		this.fromString = fromString;
		this.argument = argument;
		this.argument_to_string = argument_to_string;
	}

	public void fromString(String s) {
		if (s.startsWith(getName() + ":")) {
			this.setValue(this.fromString.apply(s.replace(this.getName() + ":", "")));
		} else {
			this.setValue(this.fromString.apply(s));
		}
	}

	public void setValue(T v) {
		this.value = v;
	}

	public String toString() {
		return this.name + ":" + String.valueOf(this.value);
	}

	public T getValue() {
		return value;
	}

	public String getName() {
		return this.name;
	}

	public ArgumentType<?> getArgument() {
		return this.argument;
	}

	public BiFunction<CommandContext<CommandSource>, String, ?> getCommandValueFunction() {
		return this.argument_to_string;
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
}