package com.silverminer.shrines.structures.harbour;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;

public class HarbourPieceType {
	public static HarbourPieceType HOUSE = new HarbourPieceType("House", new BlockPos(19, 16, 12), 4,
			new ResourceLocation("shrines:harbour/house1"));
	public static HarbourPieceType WAREHOUSE = new HarbourPieceType("Warehouse", new BlockPos(11, 6, 10), 3,
			new ResourceLocation("shrines:harbour/warehouse1"), new ResourceLocation("shrines:harbour/warehouse3"));
	public static HarbourPieceType WAREHOUSE2 = new HarbourPieceType("Warehouse 2", new BlockPos(15, 10, 11), 2,
			new ResourceLocation("shrines:harbour/warehouse2"));
	public static HarbourPieceType CRANE = new HarbourPieceType("Crane", new BlockPos(5, 10, 14), 2,
			new ResourceLocation("shrines:harbour/crane"));
	public static HarbourPieceType WARE = new HarbourPieceType("Ware", new BlockPos(5, 3, 5), 4,
			new ResourceLocation("shrines:harbour/ware1"));
	public static HarbourPieceType TAVERN = new HarbourPieceType("Tavern", new BlockPos(25, 28, 23), 1,
			new ResourceLocation("shrines:harbour/tavern"));
	public static final ArrayList<HarbourPieceType> TYPES = Lists.newArrayList(TAVERN, HOUSE, WAREHOUSE, WAREHOUSE2,
			CRANE, WARE);
	private final BlockPos size;
	private final int maxStructures;
	private final ResourceLocation[] pieces;
	public int structureIn = 0;
	private final String name;

	public HarbourPieceType(String name, BlockPos size, int maxInStructure, ResourceLocation... pieces) {
		this.size = size;
		this.maxStructures = maxInStructure;
		this.pieces = pieces;
		this.name = name;
	}

	public BlockPos getSize(Rotation rot) {
		return size.rotate(rot);
	}

	public int getMaxStructures() {
		return maxStructures;
	}

	public ResourceLocation[] getPieces() {
		return pieces;
	}

	public MutableBoundingBox getBoundingBox(BlockPos pos, Rotation rot) {
		return MutableBoundingBox.createProper(pos.getX(), pos.getY(), pos.getZ(),
				pos.getX() + this.getSize(rot).getX(), pos.getY() + this.getSize(rot).getY(),
				pos.getZ() + this.getSize(rot).getZ());
	}

	public String getName() {
		return this.name;
	}
}