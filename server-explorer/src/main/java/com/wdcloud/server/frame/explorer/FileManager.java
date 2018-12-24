package com.wdcloud.server.frame.explorer;

import com.wdcloud.server.frame.explorer.factory.GlobalFactory;
import com.wdcloud.server.frame.interfaces.IFileManagerComponent;
import com.wdcloud.utils.StringUtil;
import com.wdcloud.utils.excel.ExportExcel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings(value = "unused")
@Service
public class FileManager {
    @Resource
    private GlobalFactory dmFactory;

    public void exportResource(String resourceName, Map<String, String> paramsMap, HttpServletResponse response) throws IOException {
        if (StringUtil.isEmpty(resourceName)) {
            return;
        }
        IFileManagerComponent configComponent = dmFactory.getFileManagerComponent(resourceName);
        String fileName = configComponent.getExportFileName(paramsMap);
        List<?> objects = configComponent.queryResource(paramsMap);
        if (objects != null) {
            ExportExcel exportExcel = new ExportExcel().setData(objects);
            if (fileName == null) {
                exportExcel.write(response);
            } else {
                exportExcel.write(response, fileName);
            }
            exportExcel.dispose();
        }

    }

}
