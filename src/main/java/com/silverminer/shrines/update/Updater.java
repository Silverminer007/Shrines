/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class Updater {
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

   public static void updateAll(Path oldPacketPath, Path newPacketPath) {
      try {
         Files.walk(oldPacketPath, 1).filter(Files::isDirectory).filter(p -> !p.equals(oldPacketPath)).forEach(packDirectory -> {
            String packName = packDirectory.getFileName().toString();
            Path newPackDirectory = newPacketPath.resolve(packName);
            try {
               if (!Files.exists(newPackDirectory)) {
                  Files.createDirectories(newPackDirectory);
                  Path packInfoPath = newPackDirectory.resolve("pack.mcmeta");
                  Files.writeString(packInfoPath, """
                        {
                            "pack": {
                                "description": "Updated Shrines Structures package",
                                "pack_format": 9
                            }
                        }
                                                """);
               }
               Path dataDirectory = packDirectory.resolve("data");
               Files.walk(dataDirectory, 1).filter(Files::isDirectory).filter(p -> !p.equals(dataDirectory)).forEach(nameSpacePath -> {
                  String namespace = nameSpacePath.getFileName().toString();
                  updateVariationMaterial(nameSpacePath, newPackDirectory.resolve("data").resolve(namespace),
                        Path.of("shrines", "worldgen", "variation_material"), Path.of("shrines", "random_variation", "material"), RVMUpdate::update);
               });
            } catch (IOException e) {
               e.printStackTrace();
            }
         });
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void updateVariationMaterial(@NotNull Path nameSpacePath, @NotNull Path newNameSpacePath, Path oldRelativePath, Path newRelativePath, Function<JsonElement, JsonElement> updateFunction) {
      try {
         Path variationMaterialPath = nameSpacePath.resolve(oldRelativePath);
         Path newVariationMaterialPath = newNameSpacePath.resolve(newRelativePath);
         Files.walk(variationMaterialPath, Integer.MAX_VALUE).filter(p -> !p.equals(variationMaterialPath)).forEach(materialPath -> {
            Path materialRelativePath = variationMaterialPath.relativize(materialPath);
            if (!Files.isDirectory(materialPath)) {
               Path newMaterialPath = newVariationMaterialPath.resolve(materialRelativePath);
               try {
                  String oldMaterial = Files.readString(materialPath);
                  JsonElement newMaterialJSON = updateFunction.apply(JsonParser.parseString(oldMaterial));
                  String newMaterial = GSON.toJson(newMaterialJSON);
                  if (!Files.exists(newMaterialPath.getParent())) {
                     Files.createDirectories(newMaterialPath.getParent());
                  }
                  Files.writeString(newMaterialPath, newMaterial);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}