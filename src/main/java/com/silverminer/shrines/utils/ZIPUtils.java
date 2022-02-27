/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZIPUtils {
   protected static final Logger LOGGER = LogManager.getLogger(ZIPUtils.class);

   public static void unzipFile(final byte[] zipFile, final @NotNull Path zipDirectory, final Path destinationDirectory) throws IOException {
      Path zipPath = zipDirectory.resolve("temp.zip");
      Files.write(zipPath, zipFile);
      final byte[] buffer = new byte[1024];
      final ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath.toFile()));
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
         final File newFile = newFile(destinationDirectory.toFile(), zipEntry);
         if (zipEntry.isDirectory()) {
            if (!newFile.isDirectory() && !newFile.mkdirs()) {
               throw new IOException("Failed to create directory " + newFile);
            }
         } else {
            File parent = newFile.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
               throw new IOException("Failed to create directory " + parent);
            }

            final FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
               fos.write(buffer, 0, len);
            }
            fos.close();
         }
         zipEntry = zis.getNextEntry();
      }
      zis.closeEntry();
      zis.close();
      Files.delete(zipPath);
   }

   protected static @NotNull File newFile(File destinationDir, @NotNull ZipEntry zipEntry) throws IOException {
      File destFile = new File(destinationDir, zipEntry.getName());

      String destDirPath = destinationDir.getCanonicalPath();
      String destFilePath = destFile.getCanonicalPath();

      if (!destFilePath.startsWith(destDirPath + File.separator)) {
         throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
      }

      return destFile;
   }

   public static byte[] zipDirectory(@NotNull Path directory, Path zipDirectory) throws IOException {
      Files.createDirectories(zipDirectory);
      Path destinationPath = zipDirectory.resolve("temp.zip");
      FileOutputStream fos = new FileOutputStream(destinationPath.toFile());
      ZipOutputStream zipOut = new ZipOutputStream(fos);
      File fileToZip = directory.toFile();

      zipFile(fileToZip, fileToZip.getName(), zipOut);
      zipOut.close();
      fos.close();
      byte[] packageFile = Files.readAllBytes(destinationPath);
      Files.delete(destinationPath);
      return packageFile;
   }

   private static void zipFile(@NotNull File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
      if (fileToZip.isHidden()) {
         return;
      }
      if (fileToZip.isDirectory()) {
         if (fileName.endsWith("/")) {
            zipOut.putNextEntry(new ZipEntry(fileName));
            zipOut.closeEntry();
         } else {
            zipOut.putNextEntry(new ZipEntry(fileName + "/"));
            zipOut.closeEntry();
         }
         File[] children = fileToZip.listFiles();
         if (children != null) {
            for (File childFile : children) {
               zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
         }
         return;
      }
      FileInputStream fis = new FileInputStream(fileToZip);
      ZipEntry zipEntry = new ZipEntry(fileName);
      zipOut.putNextEntry(zipEntry);
      byte[] bytes = new byte[1024];
      int length;
      while ((length = fis.read(bytes)) >= 0) {
         zipOut.write(bytes, 0, length);
      }
      fis.close();
   }
}
