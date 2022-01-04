package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StructureNovel extends ForgeRegistryEntry<StructureNovel> {
   public static final Codec<StructureNovel> CODEC = RecordCodecBuilder.create((structureNovelInstance ->
         structureNovelInstance.group(Codec.list(Codec.STRING).fieldOf("parts").forGetter(StructureNovel::getParts))
               .apply(structureNovelInstance, StructureNovel::new)
   ));
   private final List<String> parts;

   public StructureNovel(String... parts) {
      this(Arrays.asList(parts));
   }

   public StructureNovel(List<String> parts) {
      this.parts = Objects.requireNonNullElse(parts, new ArrayList<>());
   }

   @Nonnull
   public List<String> getParts() {
      return parts;
   }
}