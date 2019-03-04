package com.wdcloud.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ZipUtils {

    public static List<File> unzip(File file) throws IOException {
        List<File> files = new ArrayList<>();
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.isDirectory()) {
                Path tmpDir = Files.createTempDirectory("tempUnzip");
                File targetFile = new File(tmpDir.toFile(), zipEntry.getName());
                targetFile.createNewFile();

                Files.copy(zipFile.getInputStream(zipEntry), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                files.add(targetFile);
            }
        }

        return files;
    }

}
