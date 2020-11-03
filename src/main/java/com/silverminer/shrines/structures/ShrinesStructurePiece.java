package com.silverminer.shrines.structures;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundNBT;
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
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public abstract class ShrinesStructurePiece extends TemplateStructurePiece {
	protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructurePiece.class);

	private final ResourceLocation location;
	private final Rotation rotation;

	public ShrinesStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager,
			ResourceLocation location, BlockPos pos, Rotation rotation, int componentTypeIn) {
		super(pieceType, componentTypeIn);
		this.location = location;
		this.templatePosition = pos;
		this.rotation = rotation;
		this.setup(templateManager);
	}

	public ShrinesStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager, CompoundNBT cNBT) {
		super(pieceType, cNBT);
		this.location = new ResourceLocation(cNBT.getString("Template"));
		this.rotation = Rotation.valueOf(cNBT.getString("Rot"));
		this.setup(templateManager);
	}

	private void setup(TemplateManager templateManager) {
		Template template = templateManager.getTemplateDefaulted(this.location);
		PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
				.setMirror(Mirror.NONE).addProcessor(this.getProcessor());
		this.setup(template, this.templatePosition, placementsettings);
	}

	/**
	 * (abstract) Helper method to read subclass data from NBT
	 */
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putString("Template", this.location.toString());
		tagCompound.putString("Rot", this.rotation.name());
	}

	public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
			Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos blockPos) {
		PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
				.setMirror(Mirror.NONE).addProcessor(this.getProcessor());
		BlockPos blockpos1 = this.templatePosition
				.add(Template.transformedBlockPos(placementsettings, new BlockPos(3, 0, 0)));
		int i = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ());
		this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
		BlockPos blockpos2 = this.templatePosition;
		boolean flag = super.func_230383_a_(world, structureManager, chunkGen, rand, mbb,
				chunkPos, blockPos);

		this.templatePosition = blockpos2;
		return flag;
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
			MutableBoundingBox sbb) {
	}

	public StructureProcessor getProcessor() {
		return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
	}
}