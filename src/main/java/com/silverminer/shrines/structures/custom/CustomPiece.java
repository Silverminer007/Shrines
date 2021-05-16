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
package com.silverminer.shrines.structures.custom;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.structures.custom.helper.PieceData;
import com.silverminer.shrines.utils.custom_structures.ModTemplateManager;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
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
			List<StructurePiece> pieces, Random random, boolean useRandomVarianting, List<PieceData> parts, String name,
			boolean ignore_air, int heightBase) {
		for (PieceData pd : parts) {
			String piece = pd.path;
			BlockPos offset = new BlockPos(pd.offset.getX(), 0, pd.offset.getZ());
			int height = heightBase + pd.offset.getY();
			pieces.add(new CustomPiece.Piece(templateManager, new ResourceLocation(Shrines.MODID, name + "/" + piece),
					pos.offset(offset.rotate(rotation)), rotation, 0, random, useRandomVarianting, ignore_air, height));
		}
	}

	public static class Piece extends ColorStructurePiece {
		public boolean useRandomVarianting = false;
		public int heightOffset = 0;
		public boolean ignore_air = true;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, boolean useRandomVarianting, boolean ignore_air, int height) {
			super(StructurePieceTypes.CUSTOM, templateManager, location, pos, rotation, componentTypeIn, true);
			this.useRandomVarianting = useRandomVarianting;
			this.heightOffset = height;
			this.ignore_air = ignore_air;
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.CUSTOM, templateManager, cNBT);
			this.useRandomVarianting = cNBT.getBoolean("varianting");
			this.heightOffset = cNBT.getInt("height");
			this.ignore_air = cNBT.getBoolean("ignore_air");
		}

		@Override
		public void setup(TemplateManager templateManager) {
			MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
			try {
				Template template = new ModTemplateManager(Utils.getLocationOf("").getCanonicalFile().toPath(),
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
			tagCompound.putBoolean("ignore_air", this.ignore_air);
		}

		@Override
		protected int getHeight(ISeedReader world, BlockPos blockpos1) {
			return this.heightOffset;
		}

		@Override
		public StructureProcessor getProcessor() {
			if (this.ignore_air)
				return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
			else
				return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return this.useRandomVarianting;
		}
	}
}