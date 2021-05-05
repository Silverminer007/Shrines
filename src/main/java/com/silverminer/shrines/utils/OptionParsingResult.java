package com.silverminer.shrines.utils;

import net.minecraft.util.text.ITextComponent;

public class OptionParsingResult {
	private final boolean success;
	private ITextComponent message;

	public OptionParsingResult(boolean success, ITextComponent message) {
		this.success = success;
		this.message = message;
	}

	public ITextComponent getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return success;
	}

	public OptionParsingResult setMessage(ITextComponent message) {
		this.message = message;
		return this;
	}
}