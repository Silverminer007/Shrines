package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StructuresPackageInfo implements Comparable<StructuresPackageInfo> {
   protected static final Logger LOGGER = LogManager.getLogger(StructuresPackageInfo.class);
   public static final Codec<StructuresPackageInfo> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
               Codec.STRING.fieldOf("display_name").forGetter(StructuresPackageInfo::getDisplayName),
               Codec.STRING.fieldOf("author").forGetter(StructuresPackageInfo::getAuthor))
         .apply(instance, StructuresPackageInfo::new));

   protected final String author;
   protected String displayName;

   public StructuresPackageInfo(String displayName, String author) {
      this.displayName = displayName;
      this.author = author;
   }

   public String getDisplayName() {
      return displayName;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public String getAuthor() {
      return author;
   }

   @Override
   public int compareTo(StructuresPackageInfo o) {
      return this.getDisplayName().compareTo(o.getDisplayName());
   }
}