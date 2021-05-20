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
package com.silverminer.shrines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.core.utils.IFunctionProvider;
import com.silverminer.shrines.core.utils.custom_structures.Utils;
import com.silverminer.shrines.core.utils.proxy.IProxy;

/**
 * @author Silverminer
 *
 */
public abstract class ShrinesMod {
	public static final String MODID = "shrines";
	public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);

	protected static ShrinesMod instance;

	protected IProxy proxy;
	protected IFunctionProvider functionProvider;

	/**
	 * TODO 1.8.1-Beta2 Update player house to be compactible with lootr -> Only at evry second
	 * chest there is a structure block 
	 * TODO 1.8.1-Beta2 Move table to extra nbt to be added
	 * (simplifly)
	 */
	public ShrinesMod() {
		instance = this;
		Utils.loadCustomStructures();
	}

	public static ShrinesMod getInstance() {
		return instance;
	}

	public IProxy getProxy(){
		if(this.proxy == null) {
			this.setProxy();
		}
		return this.proxy;
	}

	public abstract void setProxy();

	public IFunctionProvider getFunctionProvider(){
		if(this.functionProvider == null) {
			this.setFunctionProvider();
		}
		return this.functionProvider;
	}

	public abstract void setFunctionProvider();
}