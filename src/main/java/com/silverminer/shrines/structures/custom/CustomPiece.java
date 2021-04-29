package com.silverminer.shrines.structures.custom;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.utils.ModTemplateManager;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class CustomPiece {
	protected static final Logger LOGGER = LogManager.getLogger(CustomPiece.class);

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, boolean useRandomVarianting, HashMap<String, BlockPos> parts,
			String name) {
		for (String piece : parts.keySet()) {
			pieces.add(new CustomPiece.Piece(templateManager, new ResourceLocation(Shrines.MODID, name + "/" + piece),
					pos.offset(parts.get(piece)), rotation, 0, random, useRandomVarianting));
		}
	}

	public static class Piece extends ColorStructurePiece {
		public boolean useRandomVarianting = false;
		public int heightOffset = 0;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, boolean useRandomVarianting) {
			super(StructurePieceTypes.CUSTOM, templateManager, location, pos, rotation, componentTypeIn, true);
			this.useRandomVarianting = useRandomVarianting;
			this.heightOffset = pos.getY();
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.CUSTOM, templateManager, cNBT);
			this.useRandomVarianting = cNBT.getBoolean("varianting");
			this.heightOffset = cNBT.getInt("height");
		}

		public void setup(TemplateManager templateManager) {
			MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
			try {
				Template template = new ModTemplateManager(server.getFile("shrines-saves").getCanonicalFile().toPath(),
						server.getFixerUpper()).getOrCreate(this.location);
				PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
						.setMirror(Mirror.NONE).addProcessor(this.getProcessor());
				this.setup(template, this.templatePosition, placementsettings);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void addAdditionalSaveData(CompoundNBT tagCompound) {
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putBoolean("varianting", this.useRandomVarianting);
			tagCompound.putInt("height", this.heightOffset);
		}

		protected int getHeight(ISeedReader world, BlockPos blockpos1) {
			return super.getHeight(world, blockpos1) + this.heightOffset;
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
		}

		public BlockPos getOffsetPos(Random rand) {
			return BlockPos.ZERO;
		}

		@Override
		protected boolean useRandomVarianting() {
			return this.useRandomVarianting;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.BALLON.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(), loot ? ShrinesLootTables.BALLON : ShrinesLootTables.EMPTY);
			}
		}
	}
}