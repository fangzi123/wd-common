package com.wdcloud.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static final int  BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩成ZIP
     * @param srcFiles 需要压缩的文件列表
     * @param out 	        压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static List<File> unzip(File file) throws IOException {
//
//        Path fileDir = Files.createTempDirectory(null);
//
//        List<File> files = new ArrayList<>();
//        ZipFile zipFile = new ZipFile(file);
//        Enumeration<? extends ZipEntry> entries = zipFile.entries();
//        while (entries.hasMoreElements()) {
//            ZipEntry zipEntry = entries.nextElement();
//            String name = zipEntry.getName();
//            if (!zipEntry.isDirectory()) {
//                Path tmpDir = Files.createTempDirectory("tempUnzip");
//                File targetFile = new File(tmpDir.toFile(), name);
//                targetFile.createNewFile();
//
//
//
//                Files.copy(zipFile.getInputStream(zipEntry), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//                files.add(targetFile);
//            } else {
//                Path dir = Paths.get(fileDir.toString(), name);
//                Files.createDirectory(dir);
//            }
//        }
//
//        return files;
//    }

    public static List<File> unzip(File file) throws IOException {
        Path fileDir = Files.createTempDirectory(null);

        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            copyZipEntiry(zipFile, zipEntry, fileDir);
        }

        return Files.list(fileDir).map(Path::toFile).collect(Collectors.toList());
    }

    private static File copyZipEntiry(ZipFile zipFile, ZipEntry zipEntry, Path parent) throws IOException {
        String name = zipEntry.getName();
        if (zipEntry.isDirectory()) {
            File dir = new File(parent.toFile(), name);
            dir.mkdir();

            return dir;
        } else {
            File targetFile = new File(parent.toFile(), name);
            targetFile.createNewFile();

            Files.copy(zipFile.getInputStream(zipEntry), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return targetFile;
        }
    }
}
