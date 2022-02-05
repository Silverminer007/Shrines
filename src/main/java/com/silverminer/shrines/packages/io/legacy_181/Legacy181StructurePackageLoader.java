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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
   protected static final Logger LOGGER = LogManager.getLogger(Legacy181StructurePackageLoader.class);
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

   private StructuresPackageWrapper loadLegacyIncludedStructures() {
      Path configPath = FMLPaths.CONFIGDIR.get().resolve("shrines-server.toml");
      CommentedFileConfig configData = CommentedFileConfig.of(configPath);
      configData.load();
      StructureDataContainer structureDataContainer = this.getLegacyIncludedStructures();
      for (StructureData structureData : structureDataContainer.getAsIterable()) {
         try {
            String key = structureData.getKey().getPath();
            SpawnConfiguration spawnConfiguration = structureData.getSpawnConfiguration();
            spawnConfiguration.setTransformLand(configData.get(Arrays.asList("structures", key, "needs_ground")));
            spawnConfiguration.setGenerate(configData.get(Arrays.asList("structures", key, "generate")));
            spawnConfiguration.setSpawn_chance(configData.get(Arrays.asList("structures", key, "spawn_chance")));
            spawnConfiguration.setDistance(configData.get(Arrays.asList("structures", key, "distance")));
            spawnConfiguration.setSeparation(configData.get(Arrays.asList("structures", key, "seperation")));
            spawnConfiguration.setSeed_modifier(configData.get(Arrays.asList("structures", key, "seed")));
            spawnConfiguration.setBiome_blacklist(configData.get(Arrays.asList("structures", key, "blacklist")));
            spawnConfiguration.setBiome_category_whitelist(configData.get(Arrays.asList("structures", key, "categories")));
            spawnConfiguration.setDimension_whitelist(configData.get(Arrays.asList("structures", key, "dimensions")));
            List<String> dimensionWhitelist = spawnConfiguration.getDimension_whitelist();
            if (dimensionWhitelist.contains("end")) {
               dimensionWhitelist.remove("end");
               dimensionWhitelist.add("the_end");
            }
            if (dimensionWhitelist.contains("nether")) {
               dimensionWhitelist.remove("nether");
               dimensionWhitelist.add("the_nether");
            }
            structureData.setVariationConfiguration(new NewVariationConfiguration(configData.<Boolean>get(Arrays.asList("structures", key, "use_random_varianting")),
                  new ArrayList<>(), new ArrayList<>()));
         } catch (NullPointerException e) {
            if (!structureData.getKey().getNamespace().equals("ballon")
                  && !structureData.getKey().getNamespace().equals("small_tempel")
                  && !structureData.getKey().getNamespace().equals("high_tempel")) {
               LOGGER.error("Failed to import legacy structure config from shrines-server.toml for structure [{}]", structureData.getKey());
            }
         }
      }
      StructuresPackageInfo structuresPackageInfo = new StructuresPackageInfo("Included Structures (Legacy Config)", "Silverm7ner");
      return new StructuresPackageWrapper(structuresPackageInfo, structureDataContainer, new StructureTemplateContainer(), new TemplatePoolContainer());
   }

   /**
    * @return all structures that are available by default in 2.x.x but with legacy (1.8.1) structure keys
    */
   private StructureDataContainer getLegacyIncludedStructures() {
      StructureDataContainer structures = new StructureDataContainer();
      structures.add(DefaultStructureConfig.ABANDONEDWITCHHOUSE_CONFIG.toStructureData());
      StructureData balloonData = DefaultStructureConfig.BALLON_CONFIG.toStructureData();
      balloonData.setKey(new ResourceLocation(ShrinesMod.MODID, "ballon"));
      structures.add(balloonData);
      structures.add(DefaultStructureConfig.BEES_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.ENDTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.FLOODEDTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.GUARDIANMEETING_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.HARBOUR_CONFIG.toStructureData());
      StructureData highTempleData = DefaultStructureConfig.HIGHTEMPLE_CONFIG.toStructureData();
      highTempleData.setKey(new ResourceLocation(ShrinesMod.MODID, "high_tempel"));
      structures.add(highTempleData);
      structures.add(DefaultStructureConfig.INFESTEDPRISON_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.JUNGLETOWER_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.MINERALTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.NETHERPYRAMID_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.NETHERSHRINE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.ORIENTALSANCTUARY_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.PLAYERHOUSE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.SHRINEOFSAVANNA_CONFIG.toStructureData());
      StructureData smallTempleData = DefaultStructureConfig.SMALLTEMPLE_CONFIG.toStructureData();
      smallTempleData.setKey(new ResourceLocation(ShrinesMod.MODID, "small_tempel"));
      structures.add(smallTempleData);
      structures.add(DefaultStructureConfig.TRADER_HOUSE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.WATCHTOWER_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.WATERSHRINE_CONFIG.toStructureData());
      return structures;
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
