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

public class HarbourPieces {
	protected static final Logger LOG = LogManager.getLogger(HarbourPieces.class);

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
	protected static final ResourceLocation[] ZOMBIE = new ResourceLocation[] {
			new ResourceLocation("shrines:harbour/zombie_villager1"),
			new ResourceLocation("shrines:harbour/zombie_villager2"),
			new ResourceLocation("shrines:harbour/zombie_villager3"),
			new ResourceLocation("shrines:harbour/zombie_villager4"),
			new ResourceLocation("shrines:harbour/zombie_villager5") };

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		int height = getStartHeigth(pos, chunkGenerator) - 6;
		pos = new BlockPos(pos.getX(), height, pos.getZ());
		LOG.info("Generating Harbourpieces on: {}, with height: {}", pos, height);
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(0),
				pos.add(new BlockPos(0, 0, 0).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(1),
				pos.add(new BlockPos(47, 0, 0).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(2),
				pos.add(new BlockPos(94, 0, 0).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(3),
				pos.add(new BlockPos(0, 0, 47).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(4),
				pos.add(new BlockPos(47, 0, 47).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(5),
				pos.add(new BlockPos(94, 0, 47).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(6),
				pos.add(new BlockPos(0, 0, 94).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(7),
				pos.add(new BlockPos(47, 0, 94).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourPiece(templateManager, GROUND.get(8),
				pos.add(new BlockPos(94, 0, 94).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, HOUSE,
				pos.add(new BlockPos(24, 0, 18).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_180), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, HOUSE,
				pos.add(new BlockPos(7, 0, 45).rotate(rotation)), rotation.add(Rotation.COUNTERCLOCKWISE_90), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, HOUSE,
				pos.add(new BlockPos(75, 0, 85).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, TAVERN,
				pos.add(new BlockPos(20, 0, 47).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE,
				pos.add(new BlockPos(43, 0, 15).rotate(rotation)), rotation.add(Rotation.NONE), 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE,
				pos.add(new BlockPos(70, 0, 25).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_90), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE,
				pos.add(new BlockPos(97, 0, 43).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_90), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WARE,
				pos.add(new BlockPos(54, 0, 57).rotate(rotation)), rotation, 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE,
				pos.add(new BlockPos(78, 0, 37).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_180), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE,
				pos.add(new BlockPos(43, 0, 89).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_180), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE,
				pos.add(new BlockPos(6, 0, 85).rotate(rotation)), rotation.add(Rotation.NONE), 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE,
				pos.add(new BlockPos(60, 0, 85).rotate(rotation)), rotation.add(Rotation.NONE), 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE,
				pos.add(new BlockPos(85, 0, 53).rotate(rotation)), rotation.add(Rotation.COUNTERCLOCKWISE_90), 0,
				random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, CRANE,
				pos.add(new BlockPos(39, 0, 28).rotate(rotation)), rotation.add(Rotation.COUNTERCLOCKWISE_90), 0,
				random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager, WAREHOUSE_BIG,
				pos.add(new BlockPos(64, 0, 8).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_90), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(20, 0, 98).rotate(rotation)), rotation.add(Rotation.COUNTERCLOCKWISE_90), 0,
				random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(57, 0, 90).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_90), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(85, 0, 9).rotate(rotation)), rotation.add(Rotation.CLOCKWISE_180), 0, random,
				height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(83, 0, 20).rotate(rotation)), rotation.add(Rotation.COUNTERCLOCKWISE_90), 0,
				random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(86, 0, 35).rotate(rotation)), rotation.add(Rotation.COUNTERCLOCKWISE_90), 0,
				random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(47, 0, 63).rotate(rotation)), rotation.add(Rotation.NONE), 0, random, height));
		pieces.add(new HarbourPieces.HarbourBuildingPiece(templateManager,
				WAREHOUSE_SMALL.get(random.nextInt(WAREHOUSE_SMALL.size())),
				pos.add(new BlockPos(87, 0, 62).rotate(rotation)), rotation.add(Rotation.NONE), 0, random, height));
		LOG.debug("Starting villager generation");
		if (Config.STRUCTURES.HARBOUR.SPAWN_VILLAGERS.get()) {
			int maxV = 20 + random.nextInt(20);
			boolean zombie = random.nextInt(10) == 0;
			for (int i = 0; i < maxV; i++) {
				pieces.add(new HarbourPieces.VillagerPiece(templateManager,
						zombie ? ZOMBIE[random.nextInt(ZOMBIE.length)] : VILLAGER,
						pos.add(new BlockPos(random.nextInt(100), 0, random.nextInt(100)).rotate(rotation)), rotation,
						0, random));
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
				if (function.equals("house1")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						if (rand.nextInt(6) == 0)
							((LockableLootTileEntity) tileentity).setLootTable(getRandomHouseLoot(rand),
									rand.nextLong());
					}
				}
				if (function.equals("house1c")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.HARBOUR, rand.nextLong());
					}
				}
				if (function.equals("house2")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						if (rand.nextInt(6) == 0)
							((LockableLootTileEntity) tileentity).setLootTable(getRandomHouseLoot(rand),
									rand.nextLong());
					}
					tileentity = worldIn.getTileEntity(pos.down(2));
					if (tileentity instanceof LockableLootTileEntity) {
						if (rand.nextInt(6) == 0)
							((LockableLootTileEntity) tileentity).setLootTable(getRandomHouseLoot(rand),
									rand.nextLong());
					}
				}
				if (function.equals("house3")) {
					worldIn.setBlockState(pos, worldIn.getBlockState(pos.down()), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos);
					if (tileentity instanceof LockableLootTileEntity) {
						if (rand.nextInt(6) == 0)
							((LockableLootTileEntity) tileentity).setLootTable(getRandomHouseLoot(rand),
									rand.nextLong());
					}
					tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						if (rand.nextInt(6) == 0)
							((LockableLootTileEntity) tileentity).setLootTable(getRandomHouseLoot(rand),
									rand.nextLong());
					}
					tileentity = worldIn.getTileEntity(pos.down(2));
					if (tileentity instanceof LockableLootTileEntity) {
						if (rand.nextInt(6) == 0)
							((LockableLootTileEntity) tileentity).setLootTable(getRandomHouseLoot(rand),
									rand.nextLong());
					}
				}
			}
		}

		public static ResourceLocation getRandomHouseLoot(Random rand) {
			if (rand.nextInt(10) > 7) {
				return ShrinesLootTables.HARBOUR;
			} else {
				return ShrinesLootTables.HARBOUR_TAVERN;
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