package com.wdcloud.server.frame.api;

import com.wdcloud.server.frame.explorer.FileManager;
import com.wdcloud.utils.WebUtil;
import com.wdcloud.base.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 导出文件
 *
 * @author csf
 */
@SuppressWarnings(value = "unused")
@Controller
public class FileExportController{

    @Autowired
    private FileManager fileManager;
    /**
     * 导出列表
     *
     * @param resourceName 接口名称
     * @param request      请求
     * @param response     请求返回
     */
    @ResponseBody
    @RequestMapping(value = "/{resourceName}/excel", method = RequestMethod.GET, produces = {"application/vnd.ms-excel"})
    public void exportExcel(@PathVariable("resourceName") String resourceName,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, String> paramMap = WebUtil.requestToMap(request);
        try {
            fileManager.exportResource(resourceName, paramMap, response);
        } catch (IOException e) {
            throw new BaseException("导出文件失败", e);
        }
    }
}
