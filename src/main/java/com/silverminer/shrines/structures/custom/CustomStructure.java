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
package com.silverminer.shrines.structures.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

public class CustomStructure extends ShrinesStructure {
	protected static final Logger LOG = LogManager.getLogger(CustomStructure.class);
	@SuppressWarnings("unused")
	private CustomStructureData csd;

	public CustomStructure(String name, CustomStructureData csd) {
		super(name, csd);
		this.csd = csd;
	}
}