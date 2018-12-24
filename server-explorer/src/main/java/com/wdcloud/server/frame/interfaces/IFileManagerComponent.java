package com.wdcloud.server.frame.interfaces;

import java.util.List;
import java.util.Map;

public interface IFileManagerComponent {
    /**
     * 查询导出资源
     *
     * @param param 查询条件
     * @return 查询List结果
     */
    List<?> queryResource(Map<String, String> param);

    /**
     * 生成文件名
     *
     * @return 文件名
     */
    String getExportFileName(Map<String, String> param);
}
