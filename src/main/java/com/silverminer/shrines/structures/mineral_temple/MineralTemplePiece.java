/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.mineral_temple;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.utils.StructureUtils;

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
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class MineralTemplePiece {
    private static final ResourceLocation location = new ResourceLocation("shrines:mineral_temple/mineral_temple");

    public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
                                List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
        int size = 32;
        MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
        mbb.move(pos);
        int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb,
                random);
        pieces.add(new MineralTemplePiece.Piece(templateManager, location, pos, rotation, 0, height));
    }

    public static class Piece extends ColorStructurePiece {
        public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
                     int componentTypeIn, int height) {
            super(StructurePieceTypes.MINERAL_TEMPLE, templateManager, location, pos, rotation, componentTypeIn, true,
                    height);
        }

        public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
            super(StructurePieceTypes.MINERAL_TEMPLE, templateManager, cNBT);
        }

        @Override
        public StructureProcessor getProcessor() {
            return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
        }

        @Override
        protected boolean useRandomVarianting() {
            return NewStructureInit.STRUCTURES.get("mineral_temple").getConfig().getUseRandomVarianting();
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
                                        MutableBoundingBox sbb) {
            boolean loot = NewStructureInit.STRUCTURES.get("mineral_temple").getConfig().getLootChance() > rand.nextDouble();
            if ("chest".equals(function)) {
                worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
                        loot ? ShrinesLootTables.MINERAL_TEMPLE : ShrinesLootTables.EMPTY);
            }
        }
    }
}