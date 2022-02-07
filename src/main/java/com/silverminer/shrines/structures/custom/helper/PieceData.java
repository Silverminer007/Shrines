/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.custom.helper;

import net.minecraft.util.math.BlockPos;

public class PieceData {
    public final String path;
    public final BlockPos offset;

    public PieceData(String path, BlockPos offset) {
        this.path = path;
        this.offset = offset;
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

    public String toString() {
        return "[" + path + ",[" + offset.getX() + "," + offset.getY() + "," + offset.getZ() + "]]";
    }
}