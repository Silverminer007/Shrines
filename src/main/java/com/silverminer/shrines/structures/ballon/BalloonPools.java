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
package com.silverminer.shrines.structures.ballon;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.ShrinesMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;

/**
 * @author Silverminer
 *
 */
public class BalloonPools {
	public static final JigsawPattern BALLOON = JigsawPatternRegistry.register(new JigsawPattern(
			new ResourceLocation(ShrinesMod.MODID, "ballon"), new ResourceLocation("empty"),
			ImmutableList.of(
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_1").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_2").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_3").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_4").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_5").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_6").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon_7").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon2_1").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon2_2").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon2_3").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.single(new ResourceLocation(ShrinesMod.MODID, "ballon/ballon2_4").toString(),
							ProcessorLists.EMPTY), 1)),
			JigsawPattern.PlacementBehaviour.RIGID));

	public static void load() {
	}
}