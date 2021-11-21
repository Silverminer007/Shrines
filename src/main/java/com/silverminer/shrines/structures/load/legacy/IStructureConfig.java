/*
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

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome.Category;

import java.util.List;
import java.util.Locale;

/**
 * @author Silverminer
 *
 */
public interface IStructureConfig extends Comparable<IStructureConfig> {
    String getName();

    boolean getGenerate();

    double getSpawnChance();

    boolean getNeedsGround();

    int getDistance();

    int getSeparation();

    int getSeed();

    List<? extends Category> getWhitelist();

    List<? extends String> getBlacklist();

    List<? extends String> getDimensions();

    boolean getUseRandomVarianting();

    double getLootChance();

    boolean getSpawnVillagers();

    boolean isBuiltIn();

    default String getDataName() {
        return this.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    }

    boolean getActive();

    void setActive(boolean value);

    List<? extends IConfigOption<?>> getAllOptions();

    /**
     * @param option
     * @param value
     */
    default OptionParsingResult fromString(String option, String value) {
        for (IConfigOption<?> co : this.getAllOptions()) {
            if (co.getName().equals(option)) {
                OptionParsingResult res = co.fromString(value, this);
                return res;
            }
        }
        return new OptionParsingResult(false, new StringTextComponent("There is no such option as provided"));
    }
}