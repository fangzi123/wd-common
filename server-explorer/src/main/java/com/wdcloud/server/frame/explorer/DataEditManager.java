package com.wdcloud.server.frame.explorer;

import com.alibaba.fastjson.JSON;
import com.wdcloud.server.frame.exception.ParamErrorException;
import com.wdcloud.server.frame.explorer.factory.GlobalFactory;
import com.wdcloud.server.frame.interfaces.IDataLinkedHandle;
import com.wdcloud.server.frame.interfaces.ISelfDefinedEdit;
import com.wdcloud.server.frame.interfaces.LinkedHandler;
import com.wdcloud.server.frame.interfaces.OperateType;
import com.wdcloud.server.frame.interfaces.info.DataEditInfo;
import com.wdcloud.server.frame.interfaces.info.LinkedInfo;
import com.wdcloud.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 资源管理器
 * 资源增删改查接口调用管理
 *
 * @author csf
 * @Date 2016/5/9.
 */
@Slf4j
@Service
public class DataEditManager {

    @Resource
    private GlobalFactory globalFactory;


    /**
     * 资源增加f
     *
     * @param resourceName 资源名称（唯一）
     * @param beanJson     增加对象JSON串
     * @return 返回ID
     */
    @Transactional
    public LinkedInfo add(String resourceName, String beanJson, String userId) {

        log.debug("[add] add resource. resourceName={},beanJson={}", resourceName, beanJson);
        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(beanJson)) {
            log.error("[add] add resource. params is error. resourceName = {}, beanJson = {}",
                    resourceName, beanJson);
            throw new ParamErrorException();
        }
        LinkedInfo linkedInfo = globalFactory.getDataEditComponent(resourceName).add(new DataEditInfo(userId, beanJson));
        log.info("[DataEditor] add resource finish, resource={},  result={}",
                resourceName, JSON.toJSONString(linkedInfo));
        //联动处理
        return linkedHandler(resourceName, LinkedHandler.DEF_FUNCTION, OperateType.ADD, linkedInfo, userId);
    }

    /**
     * 资源修改
     *
     * @param resourceName 资源名称（唯一）
     * @param beanJson     修改对象JSON串
     */
    @Transactional
    public LinkedInfo update(String resourceName, String beanJson, String userId) {

        log.debug("[update] update resource. resourceName={},beanJson={}", resourceName, beanJson);

        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(beanJson)) {
            log.error("[update] update resource. params is error. resourceName = {}, beanJson = {}",
                    resourceName, beanJson);
            throw new ParamErrorException();
        }

        LinkedInfo linkedInfo = globalFactory.getDataEditComponent(resourceName).update(new DataEditInfo(userId, beanJson));
        log.info("[DataEditor] update resource finish, resource={}, result={}",
                resourceName, JSON.toJSONString(linkedInfo));
        //联动处理
        return linkedHandler(resourceName, LinkedHandler.DEF_FUNCTION, OperateType.EDIT, linkedInfo, userId);
    }

    /**
     * 自定义修改
     *
     * @param resourceName 资源名称（唯一）
     * @param functionName 方法名
     * @param beanJson     修改对象JSON串
     */
    @Transactional
    public LinkedInfo selfDefinedEdit(String resourceName, String functionName, String beanJson, String userId) {

        log.debug("[selfDefinedEdit] selfDefinedEdit resource. resourceName={},beanJson = {}, functionName = {}",
                resourceName, beanJson, functionName);

        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(beanJson)) {
            log.info("[selfDefinedEdit] selfDefinedEdit error. resourceName={},beanJson = {}, functionName = {}",
                    resourceName, beanJson, functionName);
            throw new ParamErrorException();
        }

        ISelfDefinedEdit selfDefinedEdit = globalFactory.getSelfDefinedEdit(resourceName, functionName);
        LinkedInfo linkedInfo = selfDefinedEdit.edit(new DataEditInfo(userId, beanJson));
        log.info("[DataEditor] self edit resource finish, resource={}, functionName={},  result={}",
                resourceName, functionName, JSON.toJSONString(linkedInfo));
        return linkedHandler(resourceName, functionName, OperateType.SELF_EDIT, linkedInfo, userId);

    }

    /**
     * 资源删除(批量）
     *
     * @param resourceName 资源名称（唯一）
     * @param idsJson      对象ID列表JSON
     */
    @Transactional
    public synchronized LinkedInfo delete(String resourceName, String idsJson, String userId) {

        log.debug("[delete] delete resource.  idsJson = {}, resourceName={}", idsJson, resourceName);

        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(idsJson)) {
            log.error("[delete] delete resource. params is error. resourceName = {}, idsJson = {}, userId ={}",
                    resourceName, idsJson);
            throw new ParamErrorException();
        }

        LinkedInfo linkedInfo = globalFactory.getDataEditComponent(resourceName).delete(new DataEditInfo(userId, idsJson));
        log.info("[DataEditor] delete resource finish, resource={},  result={}",
                resourceName, JSON.toJSONString(linkedInfo));
        //联动处理
        return linkedHandler(resourceName, LinkedHandler.DEF_FUNCTION, OperateType.DELETE, linkedInfo, userId);
    }

    /**
     * 联动处理
     *
     * @param resourceName 资源名称（唯一）
     * @param operateType  操作类别
     * @param linkedInfo   联动消息
     */
    private LinkedInfo linkedHandler(String resourceName, String functionName, OperateType operateType, LinkedInfo linkedInfo, String userId) {
        assert StringUtil.isNotEmpty(resourceName) && operateType != null;
        if (linkedInfo == null) {
            return null;
        }
        log.debug("SPDataEditManager.deletes  资源名称：" + resourceName + "  联动处理");
        linkedInfo.setUserId(userId);
        for (IDataLinkedHandle dataLinkedHandle : globalFactory.getDataLinkedHandle(resourceName, functionName, operateType)) {
            dataLinkedHandle.linkedHandle(linkedInfo);
        }

        return linkedInfo;
    }
}

