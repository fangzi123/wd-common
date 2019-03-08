package com.wdcloud.server.frame.api;

import com.alibaba.fastjson.JSON;
import com.wdcloud.server.frame.FrameConstants;
import com.wdcloud.server.frame.PageNumUtils;
import com.wdcloud.server.frame.api.utils.response.Code;
import com.wdcloud.server.frame.api.utils.response.Response;
import com.wdcloud.server.frame.explorer.DataQueryManager;
import com.wdcloud.server.frame.interfaces.info.PageQueryResult;
import com.wdcloud.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 查询接口
 *
 * @author csf
 * @Date 2016/5/9.
 */
@RestController
@RequestMapping("/")
public class DataQueryController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(DataQueryController.class);

    @Resource
    private DataQueryManager dmDataQueryManager;

    @RequestMapping(value = "{resourceName}/list", method = RequestMethod.GET, produces = {"application/javascript", "application/json"})
    public String list(@PathVariable("resourceName") String resourceName, HttpServletRequest request) {

        Map<String, String> paramMap = WebUtil.requestToMap(request);
        logger.info("[DataQuery] [list] list query. resourceName={}, condition={}", resourceName, JSON.toJSONString(paramMap));
        paramMap.put(FrameConstants.LOGIN_USER_ID,getUserId());
        List<?> list = dmDataQueryManager.list(resourceName, paramMap);
        return Response.returnResponse(Code.OK, list, Code.OK.name);
    }

    @RequestMapping(value = "{resourceName}/pageList", method = RequestMethod.GET, produces = {"application/javascript", "application/json"})
    public String pageList(@PathVariable("resourceName") String resourceName, HttpServletRequest request) {

        Map<String, String> paramMap = WebUtil.requestToMap(request);
        int pageIndex = PageNumUtils.pageIndex(paramMap);
        int pageSize = PageNumUtils.pageSize(paramMap);
        logger.info("[DataQuery] [pageList] page list query. resourceName={}, condition={}",
                resourceName, JSON.toJSONString(paramMap));
        paramMap.put(FrameConstants.LOGIN_USER_ID,getUserId());

        PageQueryResult<?> pageQueryResult = dmDataQueryManager.pageList(resourceName, paramMap, pageIndex, pageSize);
        return Response.returnResponse(Code.OK, pageQueryResult, Code.OK.name);
    }

    @RequestMapping(value = "{resourceName}/get", method = RequestMethod.GET, produces = {"application/javascript", "application/json"})
    public String findResource(@PathVariable("resourceName") String resourceName, @RequestParam(value = "data") String condition) {

        logger.info("[DataQuery] [findResource] find one object. resourceName={}, condition={}",
                resourceName, JSON.toJSONString(condition));

        Object obj = dmDataQueryManager.find(resourceName, condition);
        return Response.returnResponse(Code.OK, obj, Code.OK.name);

    }

    /**
     * 自定义查询
     *
     * @param resourceName 资源名称
     * @param functionName 方法名
     * @param request      request
     * @return 查询结果
     */
    @RequestMapping(value = "{resourceName}/{functionName}/query", method = RequestMethod.GET, produces = {"application/javascript", "application/json"})
    public String selfDefinedQuery(@PathVariable("resourceName") String resourceName,
                                   @PathVariable("functionName") String functionName,
                                   HttpServletRequest request) {

        Map<String, String> paramMap = WebUtil.requestToMap(request);
        logger.info("[DataQuery] [query] self defined query. resourceName={}, resourceName={}, condition={}",
                resourceName, functionName, JSON.toJSONString(paramMap));
        paramMap.put(FrameConstants.LOGIN_USER_ID,getUserId());

        Object search = dmDataQueryManager.search(resourceName, functionName, paramMap);
        return Response.returnResponse(Code.OK, search, Code.OK.name);

    }
}