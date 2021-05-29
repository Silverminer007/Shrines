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
	 * TODO 1.8.1 Config GUI
	 * TODO 1.8.1 Structures generation height fix
	 * TODO 1.8.1 Preview for load
	 * TODO 1.8.2 Move color structure piece from superclass to object for support of multiple pieces having the same materials
	 * TODO 1.8.2 Improve spacing of structure (make check for other structures)
	 * TODO 1.8.3 Add screen interface for setting up custom structures
	 * TODO 1.9.0 Improve Harbour generation -> IDEA: Adding it on oceans and making an island Harbour
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