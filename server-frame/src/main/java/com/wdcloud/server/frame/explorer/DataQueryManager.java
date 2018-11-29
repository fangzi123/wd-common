package com.wdcloud.server.frame.explorer;

import com.alibaba.fastjson.JSON;
import com.wdcloud.server.frame.exception.ParamErrorException;
import com.wdcloud.server.frame.explorer.factory.GlobalFactory;
import com.wdcloud.server.frame.interfaces.info.PageQueryResult;
import com.wdcloud.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资源管理器
 * 资源增删改查接口调用管理
 *
 * @author csf
 * @Date 2015/7/24.
 */
@SuppressWarnings(value = "unused")
@Service
public class DataQueryManager {

    private static Logger logger = LoggerFactory.getLogger(DataQueryManager.class);
    @Resource
    private GlobalFactory dmFactory;

    /**
     * 列表查询
     *
     * @param resourceName 资源名称（唯一）
     * @param param        条件（条件和前端约定)
     * @return 查询列表
     */
    public List<?> list(String resourceName, Map<String, String> param) {

        if (StringUtil.isEmpty(resourceName)) {
            logger.error("[SPDataQueryManager list] list query. params is error. resourceName is empty");
            throw new ParamErrorException();
        }
        logger.info("[SPDataQueryManager list] list query. resourceName={}, param={}", resourceName, JSON.toJSONString(param));

        return dmFactory.getDataQueryComponent(resourceName).list(param);
    }

    /**
     * 分页查询
     *
     * @param resourceName 资源名称（唯一）
     * @param param        条件（由request中提取参数，直接可转换成map)
     * @return 分页信息和列表
     */
    public PageQueryResult pageList(String resourceName, Map<String, String> param, int pageIndex, int pageSize) {

        if (StringUtil.isEmpty(resourceName)) {
            logger.error("[SPDataQueryManager pageList] page list query. params is error. resourceName is empty");
            throw new ParamErrorException();
        }

        logger.info("[SPDataQueryManager pageList] page list query. resourceName={}, param={}",
                resourceName, JSON.toJSONString(param));
        return dmFactory.getDataQueryComponent(resourceName).pageList(param, pageIndex, pageSize);
    }

    /**
     * 根据ID查询
     *
     * @param resourceName 资源类别称（唯一）
     * @param id           ID
     * @return 返回对象
     */
    public Object find(String resourceName, String id) {
        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(id)) {
            logger.error("[SPDataQueryManager find] find. params is error. resourceName = {},id = {}", resourceName, id);
            throw new ParamErrorException();
        }

        logger.info("[find] find object query. resourceName={}, id={}", resourceName, id);
        return dmFactory.getDataQueryComponent(resourceName).find(id);
    }

    /**
     * 自定义查询
     *
     * @param resourceName 资源名称
     * @param functionName 方法名
     * @param parameterMap 参数（条件）
     * @return 查询结果
     */
    public Object search(String resourceName, String functionName, Map<String, String> parameterMap) {

        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(functionName)) {
            logger.error("[SPDataQueryManager search] self defined query. params is error. resourceName = {},functionName = {}",
                    resourceName, functionName);
            throw new ParamErrorException();
        }

        logger.info("[SPDataQueryManager search] self defined query. resourceName={}, param={}", resourceName, JSON.toJSONString(parameterMap));
        return dmFactory.getSelfDefinedSearch(resourceName, functionName).search(parameterMap);
    }

    /**
     * 自定义查询
     *
     * @param resourceName 资源名称
     * @param parameterMap 参数（条件）
     * @return 查询结果
     */
    public Object magicQuery(String resourceName, Map<String, String> parameterMap) {

        return dmFactory.getMagicQueryComponent(resourceName).search(parameterMap);
    }
}