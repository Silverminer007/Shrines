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

import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.functions.IFunctionProvider;
import com.silverminer.shrines.utils.proxy.IProxy;

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
	 * TODO 2.0.0 Change structure system to jigsaw -> harbour
	 * TODO 2.0.0 Move custom structures to jigsaw (add GUI to define pools and assigne a pool to an structure)
	 * TODO (*) 2.0.0 Structures generation height fix -> Nether structures
	 * TODO 2.0.0 Use processors to perform Color Structure Piece's work
	 * TODO (*) 2.0.0 #8(Use a processor?)  & #13
	 * 
	 * TODO 3.0.0 Mc1.17 Update -> Move #isAir to state only version
	 * TODO 3.0.1 Preview for load
	 * 
	 * 
	 * Releases:
	 * - 2.0.0 Bug fix update
	 * - 3.0.0 Mc1.17 Update
	 * - 3.0.1 Bugfixes of 3.0.0 and some new features
	 * 
	 * NOTE: Test command: /execute positioned ~10000 ~ ~ run locate 
	 * 
	 */
	public ShrinesMod() {
		instance = this;
		Utils.loadCustomStructures();
		this.registerConfig();
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

	public abstract void registerConfig();
}