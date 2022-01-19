package com.silverminer.shrines.worldgen.structures.variation;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.packages.datacontainer.NestedVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.SimpleVariationConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class NestedVariationMaterial implements VariationMaterial {
   private final ImmutableList<VariationMaterialElement> elements;
   private final String type;

   public NestedVariationMaterial(Block plank, Block slab, Block button, Block stair, Block fence, Block fence_gate, Block log, Block stripped_log, Block trapdoor, Block door,
                                  Block standing_sign, Block wall_sign) {
      VariationMaterialElement plank1 = VariationMaterialElement.createSimpleVariationElement(plank, SimpleVariationConfiguration::arePlanksEnabled);
      VariationMaterialElement slab1 = VariationMaterialElement.createNestedVariationElement(slab, NestedVariationConfiguration::isAreSlabsEnabled,
            BlockStateAppliers::applySlabProperties);
      VariationMaterialElement button1 = VariationMaterialElement.createNestedVariationElement(button, NestedVariationConfiguration::isButtonEnabled, BlockStateAppliers::applyButtonProperties);
      VariationMaterialElement stair1 = VariationMaterialElement.createNestedVariationElement(stair, NestedVariationConfiguration::isStairEnabled, BlockStateAppliers::applyStairProperties);
      VariationMaterialElement fence1 = VariationMaterialElement.createNestedVariationElement(fence, NestedVariationConfiguration::isFenceEnabled, BlockStateAppliers::applyFenceProperties);
      VariationMaterialElement fenceGateElement = VariationMaterialElement.createNestedVariationElement(fence_gate, NestedVariationConfiguration::isFenceEnabled,
            BlockStateAppliers::applyFenceGateProperties);
      VariationMaterialElement log1 = VariationMaterialElement.createNestedVariationElement(log, NestedVariationConfiguration::isAreNormalLogsEnabled, BlockStateAppliers::applyLogProperties);
      VariationMaterialElement stripped_log1 = VariationMaterialElement.createNestedVariationElement(stripped_log, NestedVariationConfiguration::isAreStrippedLogsEnabled,
            BlockStateAppliers::applyLogProperties);
      VariationMaterialElement trapdoor1 = VariationMaterialElement.createNestedVariationElement(trapdoor, NestedVariationConfiguration::isAreTrapdoorsEnabled, BlockStateAppliers::applyTrapdoorProperties);
      VariationMaterialElement door1 = VariationMaterialElement.createNestedVariationElement(door, NestedVariationConfiguration::isAreDoorsEnabled, BlockStateAppliers::applyDoorProperties);
      VariationMaterialElement standing_sign1 = VariationMaterialElement.createNestedVariationElement(standing_sign, NestedVariationConfiguration::isStandingSignEnabled,
            BlockStateAppliers::applyStandingSignProperties);
      VariationMaterialElement wall_sign1 = VariationMaterialElement.createNestedVariationElement(wall_sign, NestedVariationConfiguration::isWallSignEnabled, BlockStateAppliers::applyWallSignProperties);
      this.elements = ImmutableList.of(plank1, slab1, button1, stair1, fence1, fenceGateElement, log1, stripped_log1, trapdoor1, door1, standing_sign1,
            wall_sign1);
      this.type = "wood";
   }

   public NestedVariationMaterial(Block block, Block slab, Block stair, Block wall) {
      VariationMaterialElement blockElement = VariationMaterialElement.createSimpleVariationElement(block, SimpleVariationConfiguration::areStonesEnabled);
      VariationMaterialElement slabElement = VariationMaterialElement.createNestedVariationElement(slab, NestedVariationConfiguration::isAreSlabsEnabled,
            BlockStateAppliers::applySlabProperties);
      VariationMaterialElement stairElement = VariationMaterialElement.createNestedVariationElement(stair, NestedVariationConfiguration::isStairEnabled, BlockStateAppliers::applyStairProperties);
      VariationMaterialElement wallElement = VariationMaterialElement.createNestedVariationElement(wall, NestedVariationConfiguration::isFenceEnabled,
            BlockStateAppliers::applyWallProperties);
      this.elements = ImmutableList.of(blockElement, slabElement, stairElement, wallElement);
      this.type = "stone";
   }

   public NestedVariationMaterial(Block wool, Block carpet){
      VariationMaterialElement woolElement = VariationMaterialElement.createSimpleVariationElement(wool, SimpleVariationConfiguration::isWoolEnabled);
      VariationMaterialElement carpetElement = VariationMaterialElement.createSimpleVariationElement(carpet, SimpleVariationConfiguration::isWoolEnabled);
      this.elements = ImmutableList.of(woolElement, carpetElement);
      this.type = "wool";
   }

   @SuppressWarnings("unused")
   public NestedVariationMaterial(ImmutableList<VariationMaterialElement> elementList, String type){
      this.elements = elementList;
      this.type = type;
   }

   @Override
   public @Nullable BlockState applyProperties(BlockState oldBlockState, @NotNull BlockState newBlockState) {
      VariationMaterialElement element = this.getElement(newBlockState.getBlock());
      return element != null ? element.applyAllProperties(oldBlockState, newBlockState) : newBlockState;
   }

   @NotNull Function<NestedVariationMaterial, Block> getNewBlockFunction(Block block) {
      for (int i = 0; i < this.elements.size(); i++) {
         if (this.elements.get(i).isMaterial(block)) {
            int finalI = i;
            return (nestedVariationMaterial -> nestedVariationMaterial.elements.get(finalI).block());
         }
      }
      return (woodenVariationMaterial -> block);
   }

   @Override
   public Block getNewBlock(VariationMaterial variationMaterial, Block block) {
      if (variationMaterial instanceof NestedVariationMaterial nestedVariationMaterial && nestedVariationMaterial.getType().equals(this.getType())) {
         return this.getNewBlockFunction(block).apply(nestedVariationMaterial);
      } else {
         return block;
      }
   }

   @Override
   public VariationMaterialElement getElement(Block block) {
      return this.elements.stream().filter(element -> element.isMaterial(block)).findFirst().orElse(null);
   }

   public String getType() {
      return type;
   }
}