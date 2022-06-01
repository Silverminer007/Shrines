/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
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
                  String author;
                  try {
                     Path packageInfoPath = packDirectory.resolve("package-info.json");
                     String packageInfo = Files.readString(packageInfoPath);
                     JsonElement jsonPackageInfo = JsonParser.parseString(packageInfo);
                     String plainAuthor = jsonPackageInfo.getAsJsonObject().get("author").getAsString();
                     author = " created by " + plainAuthor;
                  } catch (Exception e) {
                     e.printStackTrace();
                     author = "";
                  }
                  Path packInfoPath = newPackDirectory.resolve("pack.mcmeta");
                  Files.writeString(packInfoPath, """
                        {
                            "pack": {
                                "description": "Updated Shrines Structures package$author",
                                "pack_format": 9
                            }
                        }
                                                """.replace("$author", author));
               }
               Path dataDirectory = packDirectory.resolve("data");
               Files.walk(dataDirectory, 1).filter(Files::isDirectory).filter(p -> !p.equals(dataDirectory)).forEach(nameSpacePath -> {
                  String namespace = nameSpacePath.getFileName().toString();
                  Path newNameSpacePath = newPackDirectory.resolve("data").resolve(namespace);
                  try {
                     copyNamespace(nameSpacePath, newNameSpacePath);
                     update(nameSpacePath, newNameSpacePath,
                           Path.of("shrines", "worldgen", "variation_material"), Path.of("shrines", "random_variation", "material"), RVMUpdate::update);
                     SDUpdate.updateStructureData(nameSpacePath, newNameSpacePath, namespace);
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
               });
            } catch (IOException e) {
               e.printStackTrace();
            }
         });
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void copyNamespace(@NotNull Path nameSpacePath, @NotNull Path newNameSpacePath) throws IOException {
      Files.walk(nameSpacePath, 1).forEach(path -> {
         if (!path.equals(nameSpacePath) && !path.endsWith("shrines") && !path.endsWith("shrines_structures")) {
            try {
               // Copy Directory Recursively
               FileUtils.copyDirectory(path.toFile(), newNameSpacePath.resolve(nameSpacePath.relativize(path)).toFile());
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      });
   }

   private static void update(@NotNull Path nameSpacePath, @NotNull Path newNameSpacePath, Path oldRelativePath, Path newRelativePath, Function<JsonElement, JsonElement> updateFunction) {
      try {
         Path oldElement = nameSpacePath.resolve(oldRelativePath);
         Path newElement = newNameSpacePath.resolve(newRelativePath);
         Files.walk(oldElement, Integer.MAX_VALUE).filter(p -> !p.equals(oldElement)).forEach(elementPath -> {
            Path relativeElementPath = oldElement.relativize(elementPath);
            if (!Files.isDirectory(elementPath)) {
               Path newElementPath = newElement.resolve(relativeElementPath);
               try {
                  String newMaterial = GSON.toJson(updateFunction.apply(JsonParser.parseString(Files.readString(elementPath))));
                  if (!Files.exists(newElementPath.getParent())) {
                     Files.createDirectories(newElementPath.getParent());
                  }
                  Files.writeString(newElementPath, newMaterial);
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