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
package com.silverminer.shrines.structures.load.legacy;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class OptionParsingResult {
    private final boolean success;
    private Component message;

    public OptionParsingResult(boolean success, Component message) {
        this.success = success;
        this.message = message;
    }

    public Component getMessage() {
        if (this.message != null)
            return message;
        else
            return new TextComponent("");
    }

    public boolean isSuccess() {
        return success;
    }

    public OptionParsingResult setMessage(Component message) {
        this.message = message;
        return this;
    }
}