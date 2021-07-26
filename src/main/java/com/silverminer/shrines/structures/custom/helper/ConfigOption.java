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
	private T value;
	private Function<String, T> fromString;
	private Function<T, String> toString;
	private ArgumentType<?> argument;
	private BiFunction<CommandContext<CommandSource>, String, ?> argument_to_string;
	private final boolean command;

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

	public Function<String, T> getFromString(String option) {
		return fromString;
	}

	public boolean equals(Object o) {
		if (o instanceof IConfigOption) {
			return ((IConfigOption<?>) o).getName() == this.getName();
		}
		return false;
	}

	public boolean getUseInCommand() {
		return this.command;
	}

	public void setValue(T v, String structure) {
		this.value = v;
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

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
}