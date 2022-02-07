/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.guardian_meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
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
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GuardianMeetingPiece {
    private static final ArrayList<ResourceLocation> location = Lists
            .newArrayList(new ResourceLocation("shrines:guardian_meeting/guardian_meeting"));

    public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
                                List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
        int size = 32;
        MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
        mbb.move(pos);
        int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb,
                random);
        boolean flag = true;
        if (flag)
            pieces.add(new GuardianMeetingPiece.Piece(templateManager, location.get(random.nextInt(location.size())),
                    pos, rotation, 0, random, height));
        else
            // Test function for single variant
            pieces.add(new GuardianMeetingPiece.Piece(templateManager, location.get(0), pos, rotation, 0, random, height));
    }

    public static class Piece extends ColorStructurePiece {

        public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
                     int componentTypeIn, Random rand, int height) {
            super(StructurePieceTypes.GUARDIAN_MEETING, templateManager, location, pos, rotation, componentTypeIn,
                    true, height);
        }

        public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
            super(StructurePieceTypes.GUARDIAN_MEETING, templateManager, cNBT);
        }

        @Override
        public StructureProcessor getProcessor() {
            return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
        }

        @Override
        protected boolean useRandomVarianting() {
            return NewStructureInit.STRUCTURES.get("guardian_meeting").getConfig().getUseRandomVarianting();
        }

        public Block getDefaultPlank() {
            return Blocks.SPRUCE_PLANKS;
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
                                        MutableBoundingBox sbb) {
            boolean loot = NewStructureInit.STRUCTURES.get("guardian_meeting").getConfig().getLootChance() > rand.nextDouble();
            if (function.equals("chest")) {
                worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
                        loot ? ShrinesLootTables.GUARDIAN_MEETING : ShrinesLootTables.EMPTY);
            }
        }
    }
}
