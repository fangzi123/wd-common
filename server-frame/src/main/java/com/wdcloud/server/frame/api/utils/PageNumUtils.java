package com.wdcloud.server.frame.api.utils;

import com.wdcloud.server.frame.FrameConstants;
import com.wdcloud.utils.StringUtil;

import java.util.Map;

/**
 * 请求参数工具类
 *
 * @author csf
 * @Date 2015/7/27
 */
public class PageNumUtils {

    public static int pageSize(Map<String, String> paramMap) {
        int pageSize = FrameConstants.DEFAULT_PAGE_SIZE;

        if (paramMap == null || paramMap.isEmpty()) {
            return pageSize;
        }
        if (!StringUtil.isEmpty(paramMap.get(FrameConstants.PAGE_SIZE))) {
            pageSize = Integer.parseInt(paramMap.get(FrameConstants.PAGE_SIZE));
        }
        return pageSize;
    }

    public static int pageIndex(Map<String, String> paramMap) {
        //分页信息
        int pageIndex = FrameConstants.DEFAULT_PAGE_INDEX;
        if (paramMap == null || paramMap.isEmpty()) {
            return pageIndex;
        }
        if (!StringUtil.isEmpty(paramMap.get(FrameConstants.PAGE_INDEX))) {
            pageIndex = Integer.parseInt(paramMap.get(FrameConstants.PAGE_INDEX));
        }
        return pageIndex;
    }
}
