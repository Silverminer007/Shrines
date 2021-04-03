package com.silverminer.shrines.structures.harbour.test;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

public class HarbourHelper {
	public static final ResourceLocation HOUSE = new ResourceLocation("shrines:harbour/house1");

	public static final ResourceLocation TAVERN = new ResourceLocation("shrines:harbour/tavern");

	public static final ResourceLocation WARE = new ResourceLocation("shrines:harbour/ware1");
	public static final ResourceLocation CRANE = new ResourceLocation("shrines:harbour/crane");
	public static final ResourceLocation WAREHOUSE_BIG = new ResourceLocation("shrines:harbour/warehouse2");
	public static final ArrayList<ResourceLocation> WAREHOUSE_SMALL = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/warehouse1"), new ResourceLocation("shrines:harbour/warehouse3"));
	public static final ResourceLocation VILLAGER = new ResourceLocation("shrines:harbour/villager");

	public static MutableBoundingBox getBoundByPieces(ResourceLocation r) {
		MutableBoundingBox mbb = MutableBoundingBox.createProper(0, 0, 0, 0, 0, 0);
		switch (r.toString().replace("shrines:harbour/", "")) {
		case "crane":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 5, 10, 14);
			break;
		case "house1":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 19, 16, 12);
			break;
		case "tavern":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 25, 28, 23);
			break;
		case "ware1":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 5, 3, 5);
			break;
		case "warehouse1":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 11, 6, 10);
			break;
		case "warehouse2":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 15, 10, 11);
			break;
		case "warehouse3":
			mbb = MutableBoundingBox.createProper(0, 0, 0, 11, 6, 10);
			break;
		}
		return mbb;
	}

	public static ArrayList<ResourceLocation> getPossiblePieces(int i) {
		ArrayList<ResourceLocation> pieces = Lists.newArrayList();
		if (i < 10) {
			pieces = Lists.newArrayList(CRANE, WARE);
		} else if (i < 20) {
			pieces = Lists.newArrayList(CRANE, WARE);
			pieces.addAll(WAREHOUSE_SMALL);
		} else if (i < 25) {
			pieces = Lists.newArrayList(CRANE, WAREHOUSE_BIG, WARE);
			pieces.addAll(WAREHOUSE_SMALL);
		} else if (i < 35) {
			pieces = Lists.newArrayList(WAREHOUSE_BIG, HOUSE);
		} else {
			pieces = Lists.newArrayList(HOUSE, TAVERN);
		}
		return pieces;
	}

	/**
	 * Only checks x and z pos to prevent generating into each other because of
	 * Terraforming
	 * 
	 * @param mmb1        First BoundingBox
	 * @param structurebb Second BoundingBox
	 * @return true if the BoundingBoxes are intersecting
	 */
	public static boolean areBoundingBoxesIntersecting(MutableBoundingBox mmb1, MutableBoundingBox structurebb) {
		return mmb1.maxX >= structurebb.minX && mmb1.minX <= structurebb.maxX && mmb1.maxZ >= structurebb.minZ
				&& mmb1.minZ <= structurebb.maxZ;
	}

	public static boolean checkFlatness(MutableBoundingBox mbb, ChunkGenerator chunkGenerator) {
		int minheight = 256;
		int maxheight = 0;
		for (int x = mbb.minX; x < mbb.maxX; x++) {
			for (int z = mbb.minZ; z < mbb.maxZ; z++) {
				int height = chunkGenerator.getHeight(x / 16, z / 16, Heightmap.Type.WORLD_SURFACE_WG);
				minheight = Math.min(minheight, height);
				maxheight = Math.max(maxheight, height);
			}
		}
		return Math.abs(maxheight - minheight) <= 4;
	}

	public static int getStartHeigth(BlockPos pos, ChunkGenerator chunkGenerator) {
		MutableBoundingBox mbb = MutableBoundingBox.createProper(pos.getX(), 0, pos.getZ(), pos.getX() + 100, 0,
				pos.getZ() + 100);
		ArrayList<Integer> heigth = new ArrayList<Integer>();
		for (int x = mbb.minX; x < mbb.maxX; x++) {
			for (int z = mbb.minZ; z < mbb.maxZ; z++) {
				int surface = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE);
				boolean water = chunkGenerator.func_230348_a_(x / 16, z / 16)
						.getBlockState(new BlockPos(x, surface + 1, z)).getBlock() == Blocks.WATER;
				heigth.add(water ? chunkGenerator.getSeaLevel() - 1 : surface - 1);
			}
		}
		return getAverage(heigth);
	}

	public static int getAverage(ArrayList<Integer> list) {
		double summe = 0.0;

		for (int index = 0; index < list.size(); index++) {
			summe = summe + list.get(index);
		}

		if (list.size() > 0)
			return (int) (summe / list.size());
		else
			return 0;

	}
}