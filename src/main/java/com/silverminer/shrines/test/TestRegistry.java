package com.silverminer.shrines.test;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.registry.ModRegistrar;
import com.silverminer.shrines.registry.ModRegistryObject;
import net.minecraft.data.worldgen.DesertVillagePools;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class TestRegistry {
   public static final ModRegistrar<Test> REGISTRY = ModRegistrar.create(Shrines.MODID, Test.REGISTRY);
   public static final ModRegistryObject<Test> TEST_A = REGISTRY.register("test_a", () ->
         new Test(42, "goodbye", Blocks.DIAMOND_BLOCK,
               PlainVillagePools.START, Optional.empty(), Optional.empty()));
   public static final ModRegistryObject<Test> TEST_B = REGISTRY.register("test_b");
   public static final ModRegistryObject<Test> TEST_C = REGISTRY.register("test_c", () ->
         new Test(9100, "good morning", Blocks.DIAMOND_BLOCK,
               DesertVillagePools.START, Optional.ofNullable(TEST_A.get()), Optional.empty()));
}