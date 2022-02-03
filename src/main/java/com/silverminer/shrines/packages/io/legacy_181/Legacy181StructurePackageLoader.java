/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io.legacy_181;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.packages.container.*;
import com.silverminer.shrines.packages.datacontainer.*;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.packages.io.PackageIOException;
import com.silverminer.shrines.packages.io.StructurePackageLoader;
import com.silverminer.shrines.packages.io.legacy_181.configuration.LegacyStructureData;
import com.silverminer.shrines.utils.CalculationError;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Legacy181StructurePackageLoader implements StructurePackageLoader {
   private final Legacy181DirectoryStructureAccessor directoryStructureAccessor = new Legacy181DirectoryStructureAccessor();

   @Override
   public DirectoryStructureAccessor getDirectoryStructureAccessor() {
      return this.directoryStructureAccessor;
   }

   @Override
   public boolean matchesFormat() {
      return Files.isDirectory(this.getDirectoryStructureAccessor().getPackagesPath()) &&
            (Files.exists(this.getDirectoryStructureAccessor().getPackagesPath().resolve("structures" + ".txt"))
                  || Files.exists(FMLPaths.CONFIGDIR.get().resolve("shrines-server.toml")));
   }

   @Override
   public StructurePackageContainer loadPackages() throws PackageIOException {
      StructurePackageContainer structurePackageContainer = new StructurePackageContainer();
      structurePackageContainer.add(this.loadPackage(this.getDirectoryStructureAccessor().getBasePath()));
      structurePackageContainer.add(this.loadLegacyIncludedStructures());
      return structurePackageContainer;
   }

   @Override
   public StructurePackageContainer earlyLoadPackages() throws PackageIOException {
      return this.loadPackages();
   }

   private StructuresPackageWrapper loadPackage(@NotNull Path source) throws PackageIOException {
      ArrayList<LegacyStructureData> structureData = new ArrayList<>();
      File f = source.toFile();
      if (!f.exists()) {
         throw new PackageIOException(new CalculationError("Failed to load legacy structures", "Directory doesn't exists"));
      }

      File structuresFile = new File(f, "structures.txt");
      if (!structuresFile.exists()) {
         throw new PackageIOException(new CalculationError("Failed to import legacy structures", "Structures file doesn't exists"));
      }
      List<String> names;
      try {
         names = Files.readAllLines(structuresFile.toPath());
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to load legacy structure", "Failed to read structure.txt. Caused by: %s", e));
      }
      Random rand = new Random();
      for (String n : names) {
         if (n.startsWith("#"))
            continue;
         File st = new File(f, "shrines");
         st = new File(st, n);
         if (!st.isDirectory()) {
            throw new PackageIOException(new CalculationError("Failed to import legacy structure", "Directory of " + n + " doesn't exists"));
         }
         st = new File(st, n + ".txt");
         LegacyStructureData csd = new LegacyStructureData(n, rand);
         if (!st.exists()) {
            throw new PackageIOException(new CalculationError("Failed to import legacy structure", "Config file of " + n + " doesn't exists"));
         }
         StringBuilder data = new StringBuilder();
         try {
            for (String s : Files.readAllLines(st.toPath())) {
               data.append(s).append("\n");
            }
         } catch (IOException e) {
            e.printStackTrace();
            throw new PackageIOException(new CalculationError("", ""));
         }
         csd.fromString(data.toString());
         structureData.add(csd);
      }
      StructureDataContainer structures = new StructureDataContainer();
      for (LegacyStructureData legacyStructureData : structureData) {
         structures.add(legacyStructureData.toUpToDateData());
      }
      StructuresPackageInfo structuresPackageInfo = new StructuresPackageInfo("Legacy Structures package", "Legacy Package");
      return new StructuresPackageWrapper(structuresPackageInfo, structures, new StructureTemplateContainer(), new TemplatePoolContainer());
   }

   private StructuresPackageWrapper loadLegacyIncludedStructures() throws PackageIOException {
      Path configPath = FMLPaths.CONFIGDIR.get().resolve("shrines-server.toml");
      CommentedFileConfig configData = CommentedFileConfig.of(configPath);
      configData.load();
      StructureDataContainer structureDataContainer = new StructureDataContainer();
      try {
         for (String key : Arrays.asList("abandoned_witch_house", "ballon", "bees", "end_temple", "flooded_temple", "guardian_meeting", "harbour", "high_tempel", "infested_prison",
               "jungle_tower", "mineral_temple", "nether_pyramid", "nether_shrine", "oriental_sanctuary", "player_house", "small_tempel", "water_shrine"
         )) {
            ResourceLocation keyLocation = new ResourceLocation(ShrinesMod.MODID, key);
            String name = keyLocation.toString();
            boolean transformLand = configData.get(Arrays.asList("structures", key, "needs_ground"));
            boolean generate = configData.get(Arrays.asList("structures", key, "generate"));
            double spawnChance = configData.get(Arrays.asList("structures", key, "spawn_chance"));
            int distance = configData.get(Arrays.asList("structures", key, "distance"));
            int separation = configData.get(Arrays.asList("structures", key, "seperation"));
            int seedModifier = configData.get(Arrays.asList("structures", key, "seed"));
            int heightOffset = 0;
            List<String> biomeBlacklist = configData.get(Arrays.asList("structures", key, "blacklist"));
            List<String> biomeCategoryWhitelist = configData.get(Arrays.asList("structures", key, "categories"));
            List<String> dimensionWhitelist = configData.get(Arrays.asList("structures", key, "dimensions"));
            String startPool = "";
            int jigsawMaxDepth = 7;
            SpawnConfiguration spawnConfiguration = new SpawnConfiguration(transformLand, generate, spawnChance, distance, separation, seedModifier, heightOffset, biomeBlacklist,
                  biomeCategoryWhitelist, dimensionWhitelist, startPool, jigsawMaxDepth);
            VariationConfiguration variationConfiguration = configData.get(Arrays.asList("structures", key, "use_random_varianting")) ? VariationConfiguration.ALL_ENABLED :
                  VariationConfiguration.ALL_DISABLED;
            structureDataContainer.add(new StructureData(name, keyLocation, spawnConfiguration, null, null, variationConfiguration));
         }
      } catch (NullPointerException e) {
         throw new PackageIOException(new CalculationError("Failed to load structure previously saved in 1.8.1", "Config format is invalid (shrines-server.toml)"));
      }
      structureDataContainer.add(DefaultStructureConfig.SHRINEOFSAVANNA_CONFIG.toStructureData());
      structureDataContainer.add(DefaultStructureConfig.TRADER_HOUSE_CONFIG.toStructureData());
      structureDataContainer.add(DefaultStructureConfig.WATCHTOWER_CONFIG.toStructureData());
      StructuresPackageInfo structuresPackageInfo = new StructuresPackageInfo("Included Structures (Legacy Config)", "Silverm7ner");
      return new StructuresPackageWrapper(structuresPackageInfo, structureDataContainer, new StructureTemplateContainer(), new TemplatePoolContainer());
   }

   @Override
   public boolean canLoadStructureIcons() {
      return false;
   }

   @Override
   public StructureIconContainer loadStructureIcons(StructurePackageContainer packageContainer) {
      return null;
   }

   @Override
   public StructuresPackageWrapper tryImportPackage(Path source) {
      try {
         return this.loadPackage(source);
      } catch (PackageIOException e) {
         return null;
      }
   }
}
