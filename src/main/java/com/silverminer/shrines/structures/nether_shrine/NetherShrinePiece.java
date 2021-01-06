package com.silverminer.shrines.structures.nether_shrine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherShrinePiece {
	private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
			new ResourceLocation("shrines:nether_shrine/nether_shrine_001"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_002"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_003"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_004"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_005"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_006"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_007"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_008"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_009"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_010"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_011"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NetherShrinePiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
				rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.NETHER_SHRINE, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.NETHER_SHRINE, templateManager, cNBT);
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			// if (Config.STRUCTURES.GENERATE_NETHER_SHRINE_LOOT_CHANCE.get() >
			// rand.nextDouble()) {
			if (Config.STRUCTURES.NETHER_SHRINE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if ("chest1".equals(function) || "chest2".equals(function)) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.getRandomNetherLoot(rand),
								rand.nextLong());
					}
				}
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.NETHER_SHRINE.USE_RANDOM_VARIANTING.get();
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}

		public float getStoneChangeChance() {
			return 0.2F;
		}
	}
}