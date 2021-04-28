package com.silverminer.shrines.structures.guardian_meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
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

public class GuardianMeetingPiece {
	private static final ArrayList<ResourceLocation> location = Lists
			.newArrayList(new ResourceLocation("shrines:guardian_meeting/guardian_meeting"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		boolean flag = true;
		if (flag)
			pieces.add(new GuardianMeetingPiece.Piece(templateManager, location.get(random.nextInt(location.size())),
					pos, rotation, 0, random));
		else
			// Test function for single variant
			pieces.add(new GuardianMeetingPiece.Piece(templateManager, location.get(0), pos, rotation, 0, random));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand) {
			super(StructurePieceTypes.GUARDIAN_MEETING, templateManager, location, pos, rotation, componentTypeIn,
					true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.GUARDIAN_MEETING, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
		}

		public Block getDefaultPlank() {
			return Blocks.SPRUCE_PLANKS;
		}

		public boolean overwriteWool() {
			return true;
		}

		public boolean overwriteWood() {
			return true;
		}

		public boolean overwritePlanks() {
			return true;
		}

		public boolean overwriteSlabs() {
			return true;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.GUARDIAN_MEETING.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if (Config.STRUCTURES.GUARDIAN_MEETING.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					TileEntity tileentity = worldIn.getBlockEntity(pos.below());
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.GUARDIAN_MEETING,
								rand.nextLong());
					}
				}
			}
		}
	}
}
