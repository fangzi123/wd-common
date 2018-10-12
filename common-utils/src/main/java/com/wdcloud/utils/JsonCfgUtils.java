package com.wdcloud.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings(value = "unused")
public class JsonCfgUtils {
    private static final String suffix = ".json";

    private static String getFullConfigPath(final String path, final String pattern) {
        String fullPattern = pattern.endsWith(".json") ? pattern : pattern + suffix;
        return path + File.separator + fullPattern;
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFile(String filePath) throws IOException {
        StringBuilder buffer = new StringBuilder();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(filePath);
            String line; // 用来保存每行读取的内容
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            line = reader.readLine(); // 读取第一行
            while (line != null) { // 如果 line 为空说明读完了
                buffer.append(line); // 将读到的内容添加到 buffer 中
                line = reader.readLine(); // 读取下一行
            }
        } finally {
            if(reader!=null){
                is.close();
            }
            if(is!=null){
                is.close();
            }
        }
        return buffer.toString();
    }
    public static <T> List<T> parseAsList(String path, String pattern, Class<T> clazz)  {
        try {
            return JSON.parseArray(readFile(getFullConfigPath(path, pattern)), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static HashMap<String, String> jsonToMap(String condition) {

        HashMap<String, String> parameterMap = new HashMap<>();

        JSONObject jsonObject = JSON.parseObject(condition);

        if (jsonObject != null)
            jsonObject.entrySet().stream().filter(stringObjectEntry -> StringUtil.isNotEmpty(stringObjectEntry.getValue().toString())).forEach(stringObjectEntry -> {
                parameterMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
            });

        return parameterMap;
    }

}
