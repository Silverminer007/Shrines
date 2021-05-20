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
package com.silverminer.shrines.core.structures.custom.helper;

import net.minecraft.util.math.BlockPos;

public class PieceData {
	public final String path;
	public final BlockPos offset;

	public PieceData(String path, BlockPos offset) {
		this.path = path;
		this.offset = offset;
	}

	public String toString() {
		return "[" + path + ",[" + offset.getX() + "," + offset.getY() + "," + offset.getZ() + "]]";
	}

	public static PieceData fromString(String s) {
		s = s.replaceAll(" ", "");
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
		}
		String path = s.substring(0, s.indexOf("[") - 1);
		String pos = s.substring(s.indexOf("["));
		return new PieceData(path, posFromString(pos));
	}

	private static BlockPos posFromString(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
			String z = s.substring(s.lastIndexOf(",") + 1);
			s = s.substring(0, s.lastIndexOf(","));
			String y = s.substring(s.lastIndexOf(",") + 1);
			s = s.substring(0, s.lastIndexOf(","));
			String x = s;
			try {
				return new BlockPos(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
			} catch (NumberFormatException e) {
				return BlockPos.ZERO;
			}
		} else {
			return BlockPos.ZERO;
		}
	}
}