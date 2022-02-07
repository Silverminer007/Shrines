/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.prison;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
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

public class InfestedPrisonPiece {
    private static final ArrayList<ResourceLocation> location = Lists
            .newArrayList(new ResourceLocation("shrines:prison/infested_prison"));

    public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
                                List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
        int size = 32;
        MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
        mbb.move(pos);
        int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb,
                random);
        pieces.add(new InfestedPrisonPiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
                rotation, 0, true, height));
    }

    public static class Piece extends ColorStructurePiece {

        public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
                     int componentTypeIn, boolean defaultValue, int height) {
            super(StructurePieceTypes.INFESTED_PRISON, templateManager, location, pos, rotation, componentTypeIn,
                    defaultValue, height);
        }

        public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
            super(StructurePieceTypes.INFESTED_PRISON, templateManager, cNBT);
        }

        @Override
        public StructureProcessor getProcessor() {
            return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
        }

        @Override
        protected boolean useRandomVarianting() {
            return NewStructureInit.STRUCTURES.get("infested_prison").getConfig().getUseRandomVarianting();
        }

        public boolean overwriteTrapdoors() {
            return false;
        }

        public float getStoneChangeChance() {
            return 0.0005F;
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
                                        MutableBoundingBox sbb) {
            boolean loot = NewStructureInit.STRUCTURES.get("infested_prison").getConfig().getLootChance() > rand.nextDouble();
            if (function.equals("chest")) {
                worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(2),
                        loot ? ShrinesLootTables.INFECTED_PRISON : ShrinesLootTables.EMPTY);
            }
            if (function.equals("chest_cobweb")) {
                worldIn.setBlock(pos, Blocks.COBWEB.defaultBlockState(), 3);
                LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(2),
                        loot ? ShrinesLootTables.INFECTED_PRISON : ShrinesLootTables.EMPTY);
            }
        }
    }
}