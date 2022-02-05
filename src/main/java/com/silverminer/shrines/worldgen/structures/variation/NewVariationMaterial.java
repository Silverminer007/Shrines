package com.silverminer.shrines.worldgen.structures.variation;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public class NewVariationMaterial extends ForgeRegistryEntry<NewVariationMaterial> {
   public static final Codec<NewVariationMaterial> CODEC =
         RecordCodecBuilder.create(newVariationMaterialInstance ->
               newVariationMaterialInstance.group(
                           Codec.list(NewVariationMaterialElement.CODEC).fieldOf("types").forGetter(NewVariationMaterial::types),
                           Codec.STRING.fieldOf("material_id").forGetter(NewVariationMaterial::materialID),
                           Codec.BOOL.fieldOf("material_align").forGetter(NewVariationMaterial::alignMaterial))
                     .apply(newVariationMaterialInstance, NewVariationMaterial::new));
   private final List<NewVariationMaterialElement> types;
   private final String materialID;
   private final boolean materialAlign;

   public NewVariationMaterial(List<NewVariationMaterialElement> types, String materialID, boolean materialAlign) {
      this.types = ImmutableList.copyOf(types);
      this.materialID = materialID;
      this.materialAlign = materialAlign;
   }

   public NewVariationMaterialElement getElement(String typeID) {
      return this.types().stream().filter(type -> type.typeID().equals(typeID)).findFirst().orElse(null);
   }

   public NewVariationMaterialElement getElement(Block block) {
      return this.types().stream().filter(type -> type.blockID().equals(block.getRegistryName())).findFirst().orElse(null);
   }

   public List<NewVariationMaterialElement> types() {
      return this.types;
   }

   public String materialID() {
      return this.materialID;
   }

   public boolean alignMaterial() {
      return this.materialAlign;
   }
}