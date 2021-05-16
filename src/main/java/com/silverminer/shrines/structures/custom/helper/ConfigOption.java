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

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.silverminer.shrines.utils.OptionParsingResult;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public class ConfigOption<T> {
	protected static final Logger LOGGER = LogManager.getLogger(ConfigOption.class);
	private final String name;
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
		this(name, value, fromString, String::valueOf, argument, argument_to_string, true);
	}

	public <U> ConfigOption(String name, T value, Function<String, T> fromString, Function<T, String> toString, ArgumentType<U> argument,
			BiFunction<CommandContext<CommandSource>, String, U> argument_to_string, boolean command) {
		this.name = name;
		this.value = value;
		this.fromString = fromString;
		this.argument = argument;
		this.argument_to_string = argument_to_string;
		this.command = command;
		this.toString = toString;
	}

	public OptionParsingResult fromString(String s, CustomStructureData csd) {
		T v;
		if (s.startsWith(getName() + ":")) {
			v = this.fromString.apply(s.replace(this.getName() + ":", ""));
		} else {
			v = this.fromString.apply(s);
		}
		if (v == null) {
			return new OptionParsingResult(false,
					new TranslationTextComponent("commands.shrines.configure.failed.wrong_value", s, this.getName()));
		} else if (csd.distance.equals(this)) {
			if (((Integer) v) <= csd.seperation.getValue()) {
				return new OptionParsingResult(false, new TranslationTextComponent(
						"commands.shrines.configure.failed.dist_larger_sep", v, csd.seperation.getValue()));
			}
		} else if (csd.seperation.equals(this)) {
			if (((Integer) v) >= csd.distance.getValue()) {
				return new OptionParsingResult(false, new TranslationTextComponent(
						"commands.shrines.configure.failed.sep_smaller_dist", v, csd.distance.getValue()));
			}
		} else if (csd.spawn_chance.equals(this)) {
			Double d = (Double) v;
			if (d < 0.0 || d > 1.0) {
				return new OptionParsingResult(false,
						new TranslationTextComponent("commands.shrines.configure.failed.chance_out_of_range", v));
			}
		} else if (csd.seed.equals(this)) {
			Integer i = (Integer) v;
			if (i < 0) {
				return this.fromString(String.valueOf(i * -1), csd).setMessage(
						new TranslationTextComponent("commands.shrines.configure.failed.seed_set_positive"));
			} else if (i == 0) {
				return this.fromString(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)), csd)
						.setMessage(new TranslationTextComponent("commands.shrines.configure.failed.seed_set_random"));
			}
		}
		this.setValue(v);
		return new OptionParsingResult(true, null);
	}

	public boolean equals(Object o) {
		if (o instanceof ConfigOption) {
			return ((ConfigOption<?>) o).getName() == this.getName();
		} else {
			return false;
		}
	}

	public boolean getUseInCommand() {
		return this.command;
	}

	public void setValue(T v) {
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
}