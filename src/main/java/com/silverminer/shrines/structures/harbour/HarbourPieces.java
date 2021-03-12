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
import net.minecraft.block.StairsBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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

public class HarbourPieces {
	protected static final Logger LOGGER = LogManager.getLogger(HarbourPieces.class);

	protected static final ArrayList<ResourceLocation> PIECES = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/complete/harbour_p1"),
			new ResourceLocation("shrines:harbour/complete/harbour_p2"),
			new ResourceLocation("shrines:harbour/complete/harbour_p3"),
			new ResourceLocation("shrines:harbour/complete/harbour_p4"),
			new ResourceLocation("shrines:harbour/complete/harbour_p5"),
			new ResourceLocation("shrines:harbour/complete/harbour_p6"),
			new ResourceLocation("shrines:harbour/complete/harbour_p7"),
			new ResourceLocation("shrines:harbour/complete/harbour_p8"),
			new ResourceLocation("shrines:harbour/complete/harbour_p9"));

	protected static final ArrayList<ResourceLocation> PIECES2 = Lists.newArrayList(
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
		LOGGER.info("Generating Harbour on: {}", pos);
		boolean flag = true;
		if (flag) {
			rotation = Rotation.NONE;
			int height = getStartHeigth(pos, chunkGenerator) - 6;
			pos = new BlockPos(pos.getX(), height, pos.getZ());
			LOGGER.info("Generating Harbourpieces on: {}, with height: {}", pos, height);
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(0),
					pos.add(new BlockPos(0, 0, 0).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(1),
					pos.add(new BlockPos(47, 0, 0).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(2),
					pos.add(new BlockPos(94, 0, 0).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(3),
					pos.add(new BlockPos(0, 0, 47).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(4),
					pos.add(new BlockPos(47, 0, 47).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(5),
					pos.add(new BlockPos(94, 0, 47).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(6),
					pos.add(new BlockPos(0, 0, 94).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(7),
					pos.add(new BlockPos(47, 0, 94).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES2.get(8),
					pos.add(new BlockPos(94, 0, 94).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, HOUSE, pos.add(new BlockPos(24, 0, 18)),
					rotation.add(Rotation.CLOCKWISE_180), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, HOUSE, pos.add(new BlockPos(7, 0, 45)),
					rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, HOUSE, pos.add(new BlockPos(75, 0, 85)),
					rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, TAVERN, pos.add(new BlockPos(20, 0, 47)),
					rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE, pos.add(new BlockPos(43, 0, 15)),
					rotation.add(Rotation.NONE), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE, pos.add(new BlockPos(70, 0, 25)),
					rotation.add(Rotation.CLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE, pos.add(new BlockPos(97, 0, 43)),
					rotation.add(Rotation.CLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE, pos.add(new BlockPos(54, 0, 57)),
					rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE, pos.add(new BlockPos(78, 0, 37)),
					rotation.add(Rotation.CLOCKWISE_180), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE, pos.add(new BlockPos(43, 0, 89)),
					rotation.add(Rotation.CLOCKWISE_180), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE, pos.add(new BlockPos(6, 0, 85)),
					rotation.add(Rotation.NONE), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE, pos.add(new BlockPos(60, 0, 85)),
					rotation.add(Rotation.NONE), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE, pos.add(new BlockPos(85, 0, 53)),
					rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE, pos.add(new BlockPos(39, 0, 28)),
					rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WAREHOUSE_BIG,
					pos.add(new BlockPos(64, 0, 8)), rotation.add(Rotation.CLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(20, 0, 98)),
					rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(57, 0, 90)),
					rotation.add(Rotation.CLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(85, 0, 9)),
					rotation.add(Rotation.CLOCKWISE_180), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(83, 0, 20)),
					rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(86, 0, 35)),
					rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(47, 0, 63)),
					rotation.add(Rotation.NONE), 0, random, height));
			pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
					WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())), pos.add(new BlockPos(87, 0, 62)),
					rotation.add(Rotation.NONE), 0, random, height));
			if (Config.STRUCTURES.HARBOUR.SPAWN_VILLAGERS.get()) {
				int maxV = 20 + random.nextInt(20);
				for (int i = 0; i < maxV; i++) {
					pieces.add(new HarbourPieces.VillagerPiece(templateManager, VILLAGER,
							pos.add(new BlockPos(random.nextInt(100), 0, random.nextInt(100))),
							rotation.add(Rotation.NONE), 0, random, height + 7));
				}
			}
		} else {
			pos = pos.up(getStartHeigth(pos, chunkGenerator));
			MutableBoundingBox mbb = MutableBoundingBox.createProper(pos.getX(), pos.getY(), pos.getZ(), pos.getX(),
					pos.getY() + 1, pos.getZ());
			ArrayList<MutableBoundingBox> parts = new ArrayList<MutableBoundingBox>();
			int i = 0;
			int maxStructures = 20 + random.nextInt(10);
			int tries = 0;
			while (i < maxStructures && tries++ < 500) {
				BlockPos nextPos = pos.add(random.nextInt(50) * (random.nextBoolean() ? -1 : 1), 0,
						random.nextInt(50) * (random.nextBoolean() ? -1 : 1));
				for (MutableBoundingBox bb : parts) {
					if (bb.isVecInside(nextPos)) {
						continue;
					}
				}
				Rotation newRot = rotation.add(Rotation.randomRotation(random));
				HarbourPieceType type = HarbourPieceType.TYPES.get(random.nextInt(HarbourPieceType.TYPES.size()));
				if (type.structureIn >= type.getMaxStructures())
					continue;
				MutableBoundingBox newMBB = type.getBoundingBox(nextPos, newRot);
				if (!checkFlatness(newMBB, chunkGenerator))
					continue;
				for (MutableBoundingBox bb : parts) {
					if (areBoundingBoxesIntersecting(newMBB, bb)) {
						continue;
					}
				}
				pieces.add(new HarbourPieces.Piece(templateManager,
						type.getPieces()[random.nextInt(type.getPieces().length)], nextPos, newRot, 0, random, newMBB));
				parts.add(newMBB);
				mbb.expandTo(newMBB);
				i++;
				type.structureIn++;
				LOGGER.info("Generating Harbourpiece [{}] on: {}; Had found {} pieces, Pieces {}", type.getName(),
						nextPos, i, parts);
				LOGGER.info("Adding Ground Piece: {}", mbb);
				pieces.add(new HarbourPieces.GroundPiece(5, mbb, parts));
			}
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

	public static class Piece extends ColorStructurePiece {
		protected BlockPos offsetPos = BlockPos.ZERO;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, MutableBoundingBox mbb) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, location, pos, rotation, componentTypeIn, true);
			this.boundingBox = mbb;
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		public BlockPos getOffsetPos(Random rand) {
			return this.offsetPos;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HARBOUR.USE_RANDOM_VARIANTING.get();
		}

		public boolean overwriteWool() {
			return false;
		}

		public boolean overwriteBeehives() {
			return false;
		}
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
		protected int height;

		public VillagerPiece(TemplateManager templateManagerIn, ResourceLocation locationIn, BlockPos posIn,
				Rotation rotationIn, int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.HARBOUR_VILLAGER, templateManagerIn, locationIn, posIn, rotationIn,
					componentTypeIn);
			this.height = height;
		}

		public VillagerPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_VILLAGER, templateManager, cNBT);
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

		protected int getHeight(ISeedReader world, BlockPos pos) {
			return this.height;
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
				boolean flag = super.func_230383_a_(world, structureManager, chunkGen, rand, mbb, chunkPos,
						this.templatePosition);

				this.templatePosition = blockpos2;
				return flag;
			} else
				return false;
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

	public static class GroundPiece extends StructurePiece {
		protected ArrayList<MutableBoundingBox> parts = new ArrayList<MutableBoundingBox>();

		public GroundPiece(int componentTypeIn, MutableBoundingBox mbb, ArrayList<MutableBoundingBox> partsIn) {
			super(StructurePieceTypes.HARBOUR_GROUND, componentTypeIn);
			this.boundingBox = mbb;
			this.parts = partsIn;
		}

		public GroundPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_GROUND, cNBT);
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound) {
		}

		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager,
				ChunkGenerator chunkgenerator, Random rand, MutableBoundingBox mbb, ChunkPos chunkPos,
				BlockPos blockPos) {
			for (int x = boundingBox.minX; x < boundingBox.maxX; x++) {
				for (int z = boundingBox.minZ; z < boundingBox.maxZ; z++) {
					BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(x, 0, z)).down();
					for (MutableBoundingBox bb : this.parts) {
						if (bb.isVecInside(pos))
							continue;
					}
					if (!world.getBlockState(pos).getMaterial().isLiquid()) {
						BlockState state = Blocks.STONE_BRICKS.getDefaultState();
						if (rand.nextInt(8) == 0)
							state = Blocks.MOSSY_STONE_BRICKS.getDefaultState();
						else if (rand.nextInt(16) == 0)
							state = Blocks.CRACKED_STONE_BRICKS.getDefaultState();
						else if (rand.nextInt(32) == 0)
							state = Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState();
						else if (rand.nextInt(512) == 0)
							state = Blocks.WATER.getDefaultState();
						else if (rand.nextInt(256) == 0)
							state = Blocks.STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.WATERLOGGED, true)
									.with(StairsBlock.FACING, Direction.Plane.HORIZONTAL.random(rand));
						world.setBlockState(pos, state, 2);
					}
				}
			}
			return true;
		}
	}
}