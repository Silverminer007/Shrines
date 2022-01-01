package com.silverminer.shrines.packages.io.legacy_181;

import com.silverminer.shrines.packages.container.*;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageInfo;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.packages.io.PackageIOException;
import com.silverminer.shrines.packages.io.StructurePackageLoader;
import com.silverminer.shrines.packages.io.legacy_181.configuration.LegacyStructureData;
import com.silverminer.shrines.utils.CalculationError;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
      return Files.isDirectory(this.getDirectoryStructureAccessor().getPackagesPath());
   }

   @Override
   public StructurePackageContainer loadPackages() throws PackageIOException {
      StructurePackageContainer structurePackageContainer = new StructurePackageContainer();
      structurePackageContainer.add(this.loadPackage(this.getDirectoryStructureAccessor().getBasePath()));
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
         throw new PackageIOException(new CalculationError("Failed to load legacy structure", "Failed to read structure.txt. Caused by: 5s", e));
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
