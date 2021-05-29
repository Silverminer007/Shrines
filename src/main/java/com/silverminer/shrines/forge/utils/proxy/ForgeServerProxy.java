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
package com.silverminer.shrines.forge.utils.proxy;

import java.io.File;

import com.silverminer.shrines.core.utils.proxy.IProxy;

import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeServerProxy implements IProxy {

	@Override
	public File getBaseDir() {
		return FMLPaths.GAMEDIR.get().toFile();
	}
}