package com.silverminer.shrines.structures.harbour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.AbstractStructurePiece;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BetterHarbourPieces {
	protected static final Logger LOG = LogManager.getLogger(BetterHarbourPieces.class);

	protected static final ArrayList<ResourceLocation> GROUND = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p1"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p2"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p3"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p4"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p5"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p6"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p7"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p8"),
			new ResourceLocation("shrines:harbour/ground/harbour_ground_1_p9"));

	protected static final ResourceLocation HOUSE = new ResourceLocation("shrines:harbour/house1");

	protected static final ResourceLocation TAVERN = new ResourceLocation("shrines:harbour/tavern");

	protected static final ResourceLocation WARE = new ResourceLocation("shrines:harbour/ware1");
	protected static final ResourceLocation CRANE = new ResourceLocation("shrines:harbour/crane");
	protected static final ResourceLocation WAREHOUSE_BIG = new ResourceLocation("shrines:harbour/warehouse2");
	protected static final ArrayList<ResourceLocation> WAREHOUSE_SMALL = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/warehouse1"), new ResourceLocation("shrines:harbour/warehouse3"));
	protected static final ResourceLocation VILLAGER = new ResourceLocation("shrines:harbour/villager");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		int height = getStartHeigth(pos, chunkGenerator) - 6;
		pos = new BlockPos(pos.getX(), height, pos.getZ());
		LOG.info("Generating Harbourpieces on: {}, with height: {}", pos, height);
		for(int s = 1; s >= -1; s -= 2) {
			
		}
	}

	/**
	 * Only checks x and z pos to prevent generating into each other because of
	 * Terraforming
	 * 
	 * @param mmb1        First BoundingBox
	 * @param structurebb Second BoundingBox
	 * @return true if the BoundingBoxes are intersecting
	 */
	protected static boolean areBoundingBoxesIntersecting(MutableBoundingBox mmb1, MutableBoundingBox structurebb) {
		return mmb1.maxX >= structurebb.minX && mmb1.minX <= structurebb.maxX && mmb1.maxZ >= structurebb.minZ
				&& mmb1.minZ <= structurebb.maxZ;
	}

	protected static boolean checkFlatness(MutableBoundingBox mbb, ChunkGenerator chunkGenerator) {
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

	protected static int getStartHeigth(BlockPos pos, ChunkGenerator chunkGenerator) {
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

	public static class HarbourPiece extends ColorStructurePiece {
		protected int height;

		public HarbourPiece(TemplateManager templateManagerIn, ResourceLocation locationIn, BlockPos posIn,
				Rotation rotationIn, int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.HARBOUR_GROUND, templateManagerIn, locationIn, posIn, rotationIn, componentTypeIn,
					false);
			this.height = height;
		}

		public HarbourPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_GROUND, templateManager, cNBT);
			this.height = cNBT.getInt("height");
		}

		/**
		 * (abstract) Helper method to read subclass data from NBT
		 */
		protected void readAdditional(CompoundNBT tagCompound) {
			super.readAdditional(tagCompound);
			tagCompound.putInt("height", this.height);
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HARBOUR.USE_RANDOM_VARIANTING.get();
		}

		protected int getHeight(ISeedReader world, BlockPos pos) {
			return this.height;
		}

		@Override
		public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world, Random rand) {
			return newState.getBlock() == Blocks.DIRT && pos.getY() - 6 < this.getHeight(world, pos);
		}

		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
				Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos blockPos) {
			boolean flag = super.func_230383_a_(world, structureManager, chunkGen, rand, mbb, chunkPos, blockPos);
			BlockState newBlock = Blocks.DIRT.getDefaultState();
			for (Template.BlockInfo template$blockinfo : this.template.func_215381_a(this.templatePosition,
					this.placeSettings, Blocks.AIR)) {
				this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
			}
			return flag;
		}
	}

	public static class VillagerPiece extends AbstractStructurePiece {

		public VillagerPiece(TemplateManager templateManagerIn, ResourceLocation locationIn, BlockPos posIn,
				Rotation rotationIn, int componentTypeIn, Random rand) {
			super(StructurePieceTypes.HARBOUR_VILLAGER, templateManagerIn, locationIn, posIn, rotationIn,
					componentTypeIn);
		}

		public VillagerPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_VILLAGER, templateManager, cNBT);
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}

		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
				Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos blockPos) {
			PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
					.setMirror(Mirror.NONE).addProcessor(this.getProcessor());
			BlockPos blockpos1 = this.templatePosition
					.add(Template.transformedBlockPos(placementsettings, new BlockPos(3, 0, 0)));
			int i = this.getHeight(world, blockpos1);
			this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
			BlockPos blockpos2 = this.templatePosition;
			if (world.getBlockState(blockpos2).getBlock() == Blocks.AIR
					&& world.getBlockState(blockpos2.up()).getBlock() == Blocks.AIR) {
				super.func_230383_a_(world, structureManager, chunkGen, rand, mbb, chunkPos, this.templatePosition);

				this.templatePosition = blockpos2;
			}
			return true;
		}
	}

	public static class HarbourBuildingPiece extends ColorStructurePiece {
		protected int height;

		public HarbourBuildingPiece(TemplateManager templateManagerIn, ResourceLocation locationIn, BlockPos posIn,
				Rotation rotationIn, int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManagerIn, locationIn, posIn, rotationIn, componentTypeIn,
					true);
			this.height = height;
		}

		public HarbourBuildingPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, cNBT);
			this.height = cNBT.getInt("height");
			this.diamonds = cNBT.getInt("diamonds");
		}

		/**
		 * (abstract) Helper method to read subclass data from NBT
		 */
		protected void readAdditional(CompoundNBT tagCompound) {
			super.readAdditional(tagCompound);
			tagCompound.putInt("height", this.height);
			tagCompound.putInt("diamonds", this.diamonds);
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		public boolean overwriteOres() {
			return true;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			super.handleDataMarker(function, pos, worldIn, rand, sbb);
			if (Config.STRUCTURES.HARBOUR.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("warehouse1_1")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR, rand.nextLong());
					}
				}
				if (function.equals("warehouse1_2") || function.equals("warehouse1_3")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR, rand.nextLong());
					}
					tileentity = worldIn.getTileEntity(pos.down(2));
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR, rand.nextLong());
					}
					tileentity = worldIn.getTileEntity(pos.down(3));
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR, rand.nextLong());
					}
				}
				if (function.equals("chest_tavern")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down(2));
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR_TAVERN,
								rand.nextLong());
					} else {
						tileentity = worldIn.getTileEntity(pos.down(3));
						if (tileentity instanceof LockableLootTileEntity) {
							((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR_TAVERN,
									rand.nextLong());
						}
					}
				}
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HARBOUR.USE_RANDOM_VARIANTING.get();
		}

		public boolean overwriteWool() {
			return false;
		}

		protected int getHeight(ISeedReader world, BlockPos pos) {
			return this.height + 7;
		}

		protected int diamonds = 0;

		@Override
		public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world, Random rand) {
			if (newState.getBlock() == Blocks.DIAMOND_ORE) {
				if (diamonds <= 16) {
					this.diamonds++;
					return true;
				} else {
					if (COLORS.get(newState.getBlock()) == null)
						COLORS.put(newState.getBlock(), ORES.get(rand.nextInt(ORES.size())));
					world.setBlockState(pos, COLORS.get(newState.getBlock()).getDefaultState(), 3);
					return false;
				}
			}
			return true;
		}
	}
}