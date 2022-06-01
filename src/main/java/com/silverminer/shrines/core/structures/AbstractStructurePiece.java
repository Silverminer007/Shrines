/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.core.structures;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.core.utils.custom_structures.Utils;

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

public abstract class AbstractStructurePiece extends TemplateStructurePiece {
	protected static final Logger LOGGER = LogManager.getLogger(AbstractStructurePiece.class);

	protected final ResourceLocation location;
	protected final Rotation rotation;
	protected final int height;

	public AbstractStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager,
			ResourceLocation location, BlockPos pos, Rotation rotation, int componentTypeIn, int height) {
		super(pieceType, componentTypeIn);
		this.location = location;
		this.templatePosition = pos;
		this.rotation = rotation;
		this.height = height;
		LOGGER.info("Height: {}", height);
		this.setup(templateManager);
	}

	public AbstractStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager, CompoundNBT cNBT) {
		super(pieceType, cNBT);
		this.location = new ResourceLocation(cNBT.getString("Template"));
		this.rotation = Rotation.valueOf(cNBT.getString("Rot"));
		this.height = cNBT.getInt("height");
		this.setup(templateManager);
	}

	public void setup(TemplateManager templateManager) {
		Template template = templateManager.getOrCreate(this.location);
		PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
				.setMirror(Mirror.NONE).addProcessor(this.getProcessor());
		this.setup(template, this.templatePosition, placementsettings);
	}

	/**
	 * (abstract) Helper method to read subclass data from NBT
	 */
	protected void addAdditionalSaveData(CompoundNBT tagCompound) {
		super.addAdditionalSaveData(tagCompound);
		tagCompound.putString("Template", this.location.toString());
		tagCompound.putString("Rot", this.rotation.name());
		tagCompound.putInt("height", this.height);
	}

	public boolean postProcess(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
			Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos blockPos) {
		PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
				.setMirror(Mirror.NONE).addProcessor(this.getProcessor());
		BlockPos blockpos1 = this.templatePosition
				.offset(Template.calculateRelativePosition(placementsettings, new BlockPos(3, 0, 0)));
		int i = this.getHeight(world, blockpos1);
		this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
		BlockPos blockpos2 = this.templatePosition;
		boolean flag = super.postProcess(world, structureManager, chunkGen, rand, mbb, chunkPos, this.templatePosition);

		this.templatePosition = blockpos2;
		return flag;
	}

	protected int getHeight(ISeedReader world, BlockPos blockpos1) {
		if (Utils.properties.use_experimental) {
			return this.height;
		} else {
			return world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ());
		}
	}

	@Override
	protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
			MutableBoundingBox sbb) {
	}

	public StructureProcessor getProcessor() {
		return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
	}
}