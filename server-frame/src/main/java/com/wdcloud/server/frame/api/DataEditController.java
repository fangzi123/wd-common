package com.wdcloud.server.frame.api;

import com.wdcloud.server.frame.api.utils.response.Code;
import com.wdcloud.server.frame.api.utils.response.Response;
import com.wdcloud.server.frame.explorer.DataEditManager;
import com.wdcloud.server.frame.interfaces.info.LinkedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 数据修改
 *
 * @author csf
 * @Date 2016/5/9.
 */
@RestController
@RequestMapping("/")
public class DataEditController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DataEditController.class);

    @Resource
    private DataEditManager dmDataEditManager;

    @RequestMapping(value = "{resourceName}/add", method = RequestMethod.POST, produces = {"application/javascript", "application/json"})
    public String addResource(@PathVariable("resourceName") String resourceName,
                              @RequestBody String beanJson) {
        logger.info("[DataEdit] [addResource] add resource! [resourceName]:{}", resourceName);
        LinkedInfo add = dmDataEditManager.add(resourceName, beanJson, getUserId());
        return processResult(add);
    }

    @RequestMapping(value = "{resourceName}/modify", method = RequestMethod.POST, produces = {"application/javascript", "application/json"})
    public String updateResource(@PathVariable("resourceName") String resourceName,
                                 @RequestBody String beanJson) {
        logger.info("[DataEdit] [updateResource] update resource. resourceName={}", resourceName);

        LinkedInfo update = dmDataEditManager.update(resourceName, beanJson, getUserId());
        return processResult(update);
    }

    @RequestMapping(value = "{resourceName}/deletes", method = RequestMethod.POST, produces = {"application/javascript", "application/json"})
    public String deleteResources(@PathVariable("resourceName") String resourceName,
                                  @RequestBody String ids) {
        logger.info("[DataEdit] [deleteResources] delete resource. resourceName={}, ids={}", resourceName, ids);
        LinkedInfo delete = dmDataEditManager.delete(resourceName, ids, getUserId());
        return processResult(delete);
    }

    /**
     * 自定义查询
     *
     * @param resourceName 资源名称
     * @param functionName 方法名
     * @param beanJson     JSON格式数据
     * @return 查询结果
     */
    @RequestMapping(value = "{resourceName}/{functionName}/edit", method = RequestMethod.POST, produces = {"application/javascript", "application/json"})
    public String selfDefinedEdit(@PathVariable("resourceName") String resourceName,
                                  @RequestBody String beanJson,
                                  @PathVariable("functionName") String functionName) {
        logger.info("[DataEdit] [selfDefinedEdit] self defined edit function. resourceName={}, functionName={}", resourceName, functionName);
        LinkedInfo linkedInfo = dmDataEditManager.selfDefinedEdit(resourceName, functionName, beanJson, getUserId());
        return processResult(linkedInfo);
    }

    private String processResult(LinkedInfo linkedInfo) {

        if (linkedInfo == null) {
            return Response.returnResponse(Code.ERROR, Code.ERROR.name);
        }
        if (linkedInfo.msg != null) {
            return Response.returnResponse(Code.WARING_MSG, linkedInfo.msg, linkedInfo.msgParams);
        }
        return Response.returnResponse(Code.OK, linkedInfo.masterId);
    }
}