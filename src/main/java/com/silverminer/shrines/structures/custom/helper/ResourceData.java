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

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;

public class ResourceData {
	private MutableBoundingBox bounds;
	private BlockPos offset;
	private String name;

	public ResourceData(String name, MutableBoundingBox bound) {
		this(name, bound, BlockPos.ZERO);
	}

	public ResourceData(String name, MutableBoundingBox bound, BlockPos offset) {
		this.bounds = bound;
		this.setName(name);
		this.offset = offset;
	}

	public BlockPos getOffset() {
		return offset;
	}

	public void setOffset(BlockPos offset) {
		this.offset = offset;
	}

	public MutableBoundingBox getBounds() {
		return bounds;
	}

	public void setBounds(MutableBoundingBox bounds) {
		this.bounds = bounds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "ResourceData{Name:" + this.getName() + ",Bounds:" + this.getBounds().toString() + ",Offset:"
				+ this.getOffset() + "}";
	}
}