package com.wdcloud.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class WebUtil {

    public static HashMap<String, String> requestToMap(HttpServletRequest request) {

        HashMap<String, String> parameterMap = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        if (names != null) {
            Collections.list(names).stream().filter(Objects::nonNull).forEach(name -> {
                parameterMap.put(name, request.getParameter(name));
            });
        }
        return parameterMap;
    }
}
