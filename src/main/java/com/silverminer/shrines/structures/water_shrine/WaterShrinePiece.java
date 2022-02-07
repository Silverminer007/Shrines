/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.water_shrine;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.utils.StructureUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WaterShrinePiece {
    private static final ResourceLocation location = new ResourceLocation("shrines:water_shrine/water_shrine");

    public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
                                List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
        int size = 16;
        MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
        mbb.move(pos);
        int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb,
                random);
        pieces.add(new WaterShrinePiece.Piece(templateManager, location, pos, rotation, 0, height));
    }

    public static class Piece extends ColorStructurePiece {
        public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
                     int componentTypeIn, int height) {
            super(StructurePieceTypes.WATER_SHRINE, templateManager, location, pos, rotation, componentTypeIn, true,
                    height);
        }

        public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
            super(StructurePieceTypes.WATER_SHRINE, templateManager, cNBT);
        }

        @Override
        protected boolean useRandomVarianting() {
            return NewStructureInit.STRUCTURES.get("water_shrine").getConfig().getUseRandomVarianting();
        }

        public Block getDefaultPlank() {
            return Blocks.SPRUCE_PLANKS;
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
                                        MutableBoundingBox sbb) {
            boolean loot = NewStructureInit.STRUCTURES.get("water_shrine").getConfig().getLootChance() > rand.nextDouble();
            if (function.equals("chest")) {
                worldIn.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
                        loot ? ShrinesLootTables.WATER_SHRINE : ShrinesLootTables.EMPTY);
            }
        }
    }
}