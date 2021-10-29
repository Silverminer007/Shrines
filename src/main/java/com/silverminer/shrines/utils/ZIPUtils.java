package com.silverminer.shrines.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZIPUtils {
    protected static final Logger LOGGER = LogManager.getLogger(ZIPUtils.class);

    public static boolean extractArchive(File archive, File destDir) throws Exception {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                LOGGER.error("Failed to create Directory to extract Structures Packet Import");
                return false;
            }
        }

        ZipFile zipFile = new ZipFile(archive);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        byte[] buffer = new byte[16384];
        int len;

        while (entries.hasMoreElements()) {

            ZipEntry entry = entries.nextElement();

            String entryFileName = entry.getName();

            if (entry.isDirectory()) {
                File dir = new File(destDir, entryFileName);

                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        LOGGER.error("Failed to create Directory to extract Structures Packet Import");
                        return false;
                    }
                }

            } else {

                File f = new File(destDir, entryFileName);
                if(!f.getParentFile().exists() && !f.getParentFile().mkdirs()){
                    LOGGER.error("Failed to create Directory to extract Structures Packet Import");
                    return false;
                }
                bos = new BufferedOutputStream(new FileOutputStream(f));
                bis = new BufferedInputStream(zipFile.getInputStream(entry));

                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }
            }
        }

        if (bos != null) {
            bos.flush();
        }
        if (bos != null) {
            bos.close();
        }
        if (bis != null) {
            bis.close();
        }
        return true;
    }

    public static File compressArchive(File sourceDirectory, File destinationDirectory, String targetName) {
        byte[] buffer = new byte[8192];
        try {
            File destinationFile = new File(destinationDirectory, targetName + ".zip");
            int i = 1;
            while (destinationFile.exists()) {
                destinationFile = new File(destinationDirectory, targetName + " (" + i + ")" + ".zip");
                i++;
            }
            if(!destinationDirectory.exists() && !destinationDirectory.mkdirs()){
                LOGGER.error("Failed to create Export Cache Directory {}", destinationDirectory);
                return null;
            }
            FileOutputStream fos = new FileOutputStream(destinationFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            Path relativeDirectory = sourceDirectory.getParentFile().toPath();
            Files.find(sourceDirectory.toPath(), Integer.MAX_VALUE, ((path, basicFileAttributes) -> true)).forEach(path -> {
                try {
                    File fileName = path.toFile();
                    if(fileName.isDirectory()){
                        return;
                    }
                    FileInputStream fis = new FileInputStream(fileName);

                    zos.putNextEntry(new ZipEntry(relativeDirectory.relativize(path).toFile().toString()));

                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("Failed to zip File {}", path, e);
                }
            });

            zos.close();
            fos.close();

            return destinationFile;

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        return null;

    }
}
