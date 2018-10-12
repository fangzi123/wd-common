package com.wdcloud.utils.file;

import com.wdcloud.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileUtils {

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param filePath 文件全路径
     */
    public static byte[] readFileByBytes(String filePath) {
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
            // 一次读多个字节
            byte[] bytes = new byte[in.available()];
            // 读入多个字节到字节数组中，byteRead为一次读入的字节数
            in.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    public static <T> String getRootPath(Class<T> clazz) {
        URL ret = clazz.getResource("/");
        if (ret == null)
            return ".";
        try {
            return Paths.get(ret.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Path getClassRootPath(Class<T> clazz) {
        Optional<URL> url = Optional.of(clazz.getResource("/"));
        try {
            return Paths.get(url.get().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param path 路径
     * @param base 当path是相对路径时,base为起始路径
     * @return path
     */
    public static Path getDirectory(String path, String base) {
        Path dir = Paths.get(path);
        if (dir.isAbsolute()) {
            return dir;
        }
        return Paths.get(base, path);
    }

    /**
     * 获取文件后缀
     *
     * @param filePath 文件相对路径或文件名
     * @return Suffix 后缀
     */
    public static String getFileSuffix(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        } else {
            int index = filePath.lastIndexOf(".");
            if (index + 1 < filePath.length()) {
                return filePath.substring(filePath.lastIndexOf(".") + 1);
            } else {
                return "";
            }
        }
    }

    /**
     * 获取文件名 不带后缀
     *
     * @param filePath 文件相对路径或文件名
     * @return Suffix 后缀
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        } else {
            int index = filePath.lastIndexOf(".");
            if (index > 1) {
                return filePath.substring(0, filePath.lastIndexOf("."));
            } else {
                return "";
            }
        }
    }

    public static boolean createFileFolder(String filePath) {
        //创建存放分片文件的临时文件夹
        File tmpFile = new File(filePath);
        return tmpFile.exists() || tmpFile.mkdirs();
    }

    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.exists()) {
            if (!file.delete()) {
                System.gc();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                file.delete();
            }
        }
    }
}
