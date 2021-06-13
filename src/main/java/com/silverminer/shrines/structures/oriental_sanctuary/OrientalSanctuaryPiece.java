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
package com.silverminer.shrines.structures.oriental_sanctuary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.utils.StructureUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class OrientalSanctuaryPiece {
	private static final ArrayList<ResourceLocation> location = Lists
			.newArrayList(new ResourceLocation("shrines:oriental_sanctuary/oriental_sanctuary_0"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		int size = 32;
		MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
		mbb.move(pos);
		int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb,
				random);
		boolean flag = true;
		if (flag)
			pieces.add(new OrientalSanctuaryPiece.Piece(templateManager, location.get(random.nextInt(location.size())),
					pos, rotation, 0, random, height));
		else
			// Test function for single variant
			pieces.add(new OrientalSanctuaryPiece.Piece(templateManager, location.get(0), pos, rotation, 0, random, height));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.ORIENTAL_SANCTUARY, templateManager, location, pos, rotation, componentTypeIn,
					true, height);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.ORIENTAL_SANCTUARY, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		public Block getDefaultPlank() {
			return Blocks.SPRUCE_PLANKS;
		}

		@Override
		protected boolean useRandomVarianting() {
			return NewStructureInit.STRUCTURES.get("oriental_sanctuary").getConfig().getUseRandomVarianting();
		}

		public boolean overwriteStone() {
			return false;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
		}
	}
}