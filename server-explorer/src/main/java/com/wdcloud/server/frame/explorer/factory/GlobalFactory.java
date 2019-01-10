package com.wdcloud.server.frame.explorer.factory;

import com.wdcloud.server.frame.exception.ParamErrorException;
import com.wdcloud.server.frame.interfaces.*;
import com.wdcloud.server.frame.interfaces.info.DefinedFunctionInfo;
import com.wdcloud.utils.AnnotationUtils;
import com.wdcloud.utils.Assert;
import com.wdcloud.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工厂类
 * 1、管理资源增删改接口
 * 2、管理查询接口
 * 3、管理联动接口
 * 4、管理自定义查询接口
 * 5、管理自定义修改接口
 *
 * @author csf
 * @Date 2015/7/24.
 */
@Slf4j
@Service
public class GlobalFactory {
    private final Map<String, IDataEditComponent> dataEditComponentMap;
    private final Map<String, IDataQueryComponent> dataQueryComponentMap;
    private final Map<String, Map<String, ISelfDefinedSearch>> selfDefinedSearchMap;
    private final Map<String, Map<String, ISelfDefinedEdit>> selfDefinedEdit;
    private final Map<String, Map<OperateType, Map<String, List<IDataLinkedHandle>>>> dataLinkedHandleMap;
    private final Map<String, IFileManagerComponent> fileManagerComponentMap;
    private final Map<String, Map<String, IDirectComponent>> directComponentMap;

    private final Map<String, String> nameDescriptionMap;

    public GlobalFactory() {
        this.dataEditComponentMap = new HashMap<>();
        this.nameDescriptionMap = new HashMap<>();
        this.dataQueryComponentMap = new HashMap<>();
        this.selfDefinedSearchMap = new HashMap<>();
        this.selfDefinedEdit = new HashMap<>();
        this.dataLinkedHandleMap = new HashMap<>();
        this.fileManagerComponentMap = new HashMap<>();
        this.directComponentMap = new HashMap<>();
    }

    /**
     * 初始化 数据修改 IDataEditComponent 实现类
     *
     * @param dataEditComponents 接口实现
     */
    @Autowired(required = false)
    private void init(IDataEditComponent[] dataEditComponents) {
        if (dataEditComponents != null && dataEditComponents.length > 0) {
            for (IDataEditComponent dataEditComponent : dataEditComponents) {
                //获取注解信息
                ResourceInfo info = AnnotationUtils.getAnnotation(dataEditComponent, ResourceInfo.class);
                if (info != null) {
                    //判断资源是否冲突
                    initResourceInfo(dataEditComponentMap, info);
                    dataEditComponentMap.put(info.name(), dataEditComponent);
                }
            }
        }
    }

    /**
     * 初始化 通用查询 IDataQueryComponent 实现类
     *
     * @param dataQueryComponents 接口实现
     */
    @Autowired(required = false)
    private void init(IDataQueryComponent[] dataQueryComponents) {
        if (dataQueryComponents != null && dataQueryComponents.length > 0) {
            for (IDataQueryComponent dataQueryComponent : dataQueryComponents) {
                //获取注解信息
                ResourceInfo info = AnnotationUtils.getAnnotation(dataQueryComponent, ResourceInfo.class);
                if (info != null) {
                    //判断资源是否冲突
                    initResourceInfo(dataQueryComponentMap, info);
                    dataQueryComponentMap.put(info.name(), dataQueryComponent);
                }
            }
        }
    }

    /**
     * 初始化 文件管理 IFileManagerComponent 实现类
     *
     * @param fileManagerComponents 接口实现
     */
    @Autowired(required = false)
    private void init(IFileManagerComponent[] fileManagerComponents) {
        if (fileManagerComponents != null && fileManagerComponents.length > 0) {
            for (IFileManagerComponent dataQueryComponent : fileManagerComponents) {
                //获取注解信息
                ResourceInfo info = AnnotationUtils.getAnnotation(dataQueryComponent, ResourceInfo.class);
                if (info != null) {
                    //判断资源是否冲突
                    initResourceInfo(fileManagerComponentMap, info);
                    fileManagerComponentMap.put(info.name(), dataQueryComponent);
                }
            }
        }
    }

    /**
     * 初始化 联动修改接口
     *
     * @param dataLinkedHandles 接口实现
     */
    @Autowired(required = false)
    private void init(IDataLinkedHandle[] dataLinkedHandles) {

        if (dataLinkedHandles != null && dataLinkedHandles.length > 0) {
            for (IDataLinkedHandle dataLinkedHandle : dataLinkedHandles) {
                //获取注解信息
                LinkedHandler linkedHandler = AnnotationUtils.getAnnotation(dataLinkedHandle, LinkedHandler.class);

                //当对象和注解信息不为空
                if (dataLinkedHandle != null && linkedHandler != null) {

                    OperateType operateType = linkedHandler.operateType();
                    String dependResourceName = linkedHandler.dependResourceName();
                    String dependFunctionName = linkedHandler.dependFunctionName();

                    if (!dataLinkedHandleMap.containsKey(dependResourceName)) {
                        dataLinkedHandleMap.put(dependResourceName, new HashMap<>());
                    }

                    Map<OperateType, Map<String, List<IDataLinkedHandle>>> operateTypeListMap = dataLinkedHandleMap.get(dependResourceName);
                    if (!operateTypeListMap.containsKey(operateType)) {
                        operateTypeListMap.put(operateType, new HashMap<>());
                    }
                    Map<String, List<IDataLinkedHandle>> dataLinkedHandleFunMap = operateTypeListMap.get(operateType);
                    if (!dataLinkedHandleFunMap.containsKey(dependFunctionName)) {
                        dataLinkedHandleFunMap.put(dependFunctionName, new ArrayList<>());
                    }
                    dataLinkedHandleFunMap.get(dependFunctionName).add(dataLinkedHandle);
//
//                    if(true){
//                        Map<OperateType, Map<String, List<IDataLinkedHandle>>> operateTypeListMap = dataLinkedHandleMap.get(dependResourceName);
//                        Map<String, List<IDataLinkedHandle>> dataLinkedHandleFunMap = operateTypeListMap.get(operateType);
//                        if (dataLinkedHandleFunMap == null) {
//                            operateTypeListMap.put(operateType, new HashMap<>());
//                            operateTypeListMap.get(operateType).put("", new ArrayList<>());
//                            dataLinkedHandleFunMap = operateTypeListMap.get(operateType);
//                        }
//
//                        if (!dataLinkedHandleFunMap.containsKey(dependFunctionName)) {
//                            dataLinkedHandleFunMap.put(dependFunctionName, new ArrayList<>());
//                        }
//                        dataLinkedHandleFunMap.get(dependFunctionName).add(dataLinkedHandle);
//
////                        if (dataLinkedHandleFunMap != null && !dataLinkedHandleFunMap.isEmpty()) {
////                            //如果原类别Map存在此操作类别的列表，在列表中增加即可
////                            dataLinkedHandleFunMap.add(dataLinkedHandle);
////                        } else {
////                            //如果原类别Map中不存在，新增加此类别列表
////                            dataLinkedHandleFunMap = new ArrayList<>();
////                            dataLinkedHandleFunMap.add(dataLinkedHandle);
////                            operateTypeListMap.put(operateType, dataLinkedHandleFunMap);
////                        }
//                    } else {
//                        //如果原资源类别Map中不存在，新增加此资源类别列表
//                        Map<OperateType, List<IDataLinkedHandle>> operateTypeListMap = new HashMap<>();
//                        List<IDataLinkedHandle> dataLinkedHandleList = new ArrayList<>();
//                        dataLinkedHandleList.add(dataLinkedHandle);
//                        operateTypeListMap.put(operateType, dataLinkedHandleList);
//                        dataLinkedHandleMap.put(dependResourceName, operateTypeListMap);
//                    }
                }
            }
        }
    }

    @Autowired(required = false)
    private void init(IDirectComponent[] directComponents) {
        if (directComponents == null) {
            return;
        }
        for (IDirectComponent directComponent : directComponents) {
            IDirectComponent.DirectHandler directHandler = AnnotationUtils.getAnnotation(directComponent, IDirectComponent.DirectHandler.class);
            if (directHandler == null || StringUtil.isEmpty(directHandler.resource()) || StringUtil.isEmpty(directHandler.function())) {
                continue;
            }

            String resource = directHandler.resource();
            String function = directHandler.function();

            if (!directComponentMap.containsKey(resource)) {
                directComponentMap.put(resource, new HashMap<>());
            }

            if (!directComponentMap.get(resource).containsKey(function)) {
                directComponentMap.get(resource).put(function, directComponent);
            } else {
                throw new FactoryException("DirectComponent资源：" + resource + "已存在");
            }
        }
    }

    /**
     * 判断资源是否冲突
     *
     * @param map  接口容器
     * @param info 资源注解
     */
    private synchronized void initResourceInfo(Map<String, ?> map, ResourceInfo info) {

        assert map != null && info != null && !StringUtil.isEmpty(info.name());

        if (map.containsKey(info.name())) {
            if (nameDescriptionMap.containsKey(info.name())) {
                log.error("资源：{} 已存在 与（资源简介）：{} 冲突", info.name(), nameDescriptionMap.get(info.name()));
                throw new FactoryException("资源：" + info.name() + "已存在 与（资源简介）：" + nameDescriptionMap.get(info.name()) + "冲突");
            } else {
                nameDescriptionMap.put(info.name(), info.description());
                log.error("资源：" + info.name() + "已存在");
                throw new FactoryException("资源：" + info.name() + "已存在");
            }
        }
    }

    public IDirectComponent getDirectComponent(String resourceName, String functionName) {

        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(functionName)) {
            throw new FactoryException("params.error");
        }

        if (!directComponentMap.containsKey(resourceName)) {
            throw new FactoryException("interface.not.exist.error");
        }
        log.debug("获取DirectComponent资源：{}", resourceName);
        IDirectComponent directComponent = directComponentMap.get(resourceName).get(functionName);
        if (directComponent == null) {
            throw new FactoryException("interface.not.exist.error");
        }
        return directComponent;
    }

    /**
     * 通过资源类别，获取资源数据修改实现类
     *
     * @param resourceName 资源名称
     * @return 资源数据修改实现类
     */
    public IDataEditComponent getDataEditComponent(String resourceName) {

        if (StringUtil.isEmpty(resourceName)) {
            log.warn("资源:{} 不存在 数据修改接口 实现", resourceName);
            throw new FactoryException("params.error");
        }

        if (!dataEditComponentMap.containsKey(resourceName)) {
            throw new FactoryException("interface.not.exist.error");
        }
        log.debug("获取资源：{} 数据修改接口", resourceName);
        return dataEditComponentMap.get(resourceName);
    }

    /**
     * 通过资源类别，获取资源数据查询实现类
     *
     * @param resourceName 资源名称
     * @return 资源数据查询实现类
     */
    public IDataQueryComponent getDataQueryComponent(String resourceName) {

        if (StringUtil.isEmpty(resourceName)) {
            log.error("getDataQueryComponent 资源名为空");
            throw new FactoryException("params.error");
        }

        if (!dataQueryComponentMap.containsKey(resourceName)) {
            log.error("资源:{}不存在 查询接口", resourceName);
            throw new FactoryException("interface.not.exist.error");
        }

        log.debug("获取资源：{} 查询接口", resourceName);
        return dataQueryComponentMap.get(resourceName);
    }

    /**
     * 通过资源类别，获取资源联动实现类
     *
     * @param resourceName 资源名称
     * @param operateType  操作类别
     * @return 联动接口实现列表
     */
    public List<IDataLinkedHandle> getDataLinkedHandle(String resourceName, String functionName, OperateType operateType) {
        if (StringUtil.isEmpty(resourceName) || null == operateType) {
            log.warn("获取资源联动实现时 资源名称或操作类别不能为空");
            return new ArrayList<>();
        }

        if (dataLinkedHandleMap.containsKey(resourceName)) {
            Map<OperateType, Map<String, List<IDataLinkedHandle>>> operateTypeListMap = dataLinkedHandleMap.get(resourceName);
            if (operateTypeListMap.containsKey(operateType)) {
                log.debug("获取资源：{}   操作类别：{}  联动接口", resourceName, operateType);
                return operateTypeListMap.get(operateType).get(functionName);
            }
        }
        return new ArrayList<>();
    }

    //start 自定义查询接口初始化

    /**
     * 初始化 自定义查询实现
     *
     * @param selfDefinedSearchMap 接口实现
     */
    @Autowired(required = false)
    private void init(Map<String, ISelfDefinedSearch> selfDefinedSearchMap) {

        if (selfDefinedSearchMap != null && !selfDefinedSearchMap.isEmpty()) {
            for (Map.Entry<String, ISelfDefinedSearch> entry : selfDefinedSearchMap.entrySet()) {
                ISelfDefinedSearch selfDefinedSearch = entry.getValue();
                DefinedFunctionInfo definedFunction = getFunctionName(selfDefinedSearch);
                if (!this.selfDefinedSearchMap.containsKey(definedFunction.resourceName)) {
                    this.selfDefinedSearchMap.put(definedFunction.resourceName, new HashMap<>());
                }
                if (getSelfDefinedSearchMap(definedFunction.resourceName).containsKey(definedFunction.functionName)) {
                    throw new FactoryException("自定义查询函数命名冲突: resourceName="+definedFunction.resourceName+",functionName="+definedFunction.functionName);
                }
                getSelfDefinedSearchMap(definedFunction.resourceName).put(definedFunction.functionName, selfDefinedSearch);
            }
        }
    }

    /**
     * 获取自定义查询实现类
     *
     * @param resourceName 资源名称
     * @param functionName 方法名
     * @return 查询实现类
     */
    public ISelfDefinedSearch getSelfDefinedSearch(String resourceName, String functionName) {
        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(functionName)) {
            log.error("getSelfDefinedSearch 资源名,方法名为空");
            throw new FactoryException("params.error");
        }
        Map<String, ISelfDefinedSearch> functorMap = getSelfDefinedSearchMap(resourceName);
        ISelfDefinedSearch ret = functorMap.get(functionName);
        if (ret == null) {
            log.error("资源:{},方法名：{} 不存在 获取自定义查询实现类", functionName, functionName);
            throw new FactoryException("interface.not.exist.error");
        }
        return ret;
    }

    private Map<String, ISelfDefinedSearch> getSelfDefinedSearchMap(String resourceName) {
        Map<String, ISelfDefinedSearch> ret = selfDefinedSearchMap.get(resourceName);
        if (ret == null) {
            log.error("资源:{}不存在 获取自定义查询实现类", resourceName);
            throw new FactoryException("interface.not.exist.error");
        }
        return ret;
    }
    //end 自定义查询接口初始化

    /**
     * 获取资源信息
     *
     * @param tClass 自定义查询实现类
     * @return 方法名
     */
    private <T> DefinedFunctionInfo getFunctionName(T tClass) {
        Assert.notNull(tClass, " 实例不可为空");

        SelfDefinedFunction annotation = AnnotationUtils.getAnnotation(tClass, SelfDefinedFunction.class);
        if (annotation == null) {
            throw new FactoryException("实现类未定义方法名");
        } else {
            return new DefinedFunctionInfo(annotation.resourceName(), annotation.functionName());

        }
    }

    // start 自定义查询接口初始化

    /**
     * 初始化 自定义更新实现
     *
     * @param selfDefinedEditMap 接口实现
     */
    @Autowired(required = false)
    private void initEdit(Map<String, ISelfDefinedEdit> selfDefinedEditMap) {

        if (selfDefinedEditMap != null && !selfDefinedEditMap.isEmpty()) {
            for (Map.Entry<String, ISelfDefinedEdit> entry : selfDefinedEditMap.entrySet()) {
                ISelfDefinedEdit selfDefinedEdit = entry.getValue();
                DefinedFunctionInfo definedFunction = getFunctionName(selfDefinedEdit);
                if (!this.selfDefinedEdit.containsKey(definedFunction.resourceName)) {
                    this.selfDefinedEdit.put(definedFunction.resourceName, new HashMap<>());
                }
                if (getSelfDefinedEditMap(definedFunction.resourceName).containsKey(definedFunction.functionName)) {
                    throw new FactoryException("查询函数命名冲突");
                }
                getSelfDefinedEditMap(definedFunction.resourceName).put(definedFunction.functionName, selfDefinedEdit);
            }
        }
    }

    /**
     * 获取自定义更新方法实现类
     *
     * @param resourceName 资源名称
     * @param functionName 方法名
     * @return 更新实现类
     */
    public ISelfDefinedEdit getSelfDefinedEdit(String resourceName, String functionName) {
        if (StringUtil.isEmpty(resourceName) || StringUtil.isEmpty(functionName)) {
            log.error("资源名,方法名为空");
            throw new FactoryException("params.error");
        }
        Map<String, ISelfDefinedEdit> functionMap = getSelfDefinedEditMap(resourceName);
        ISelfDefinedEdit ret = functionMap.get(functionName);
        if (ret == null) {
            log.error("资源:{},方法名：{} 不存在 获取自定义修改实现类", functionName, functionName);
            throw new FactoryException("interface.not.exist.error");
        }
        return ret;
    }

    private Map<String, ISelfDefinedEdit> getSelfDefinedEditMap(String resourceName) {

        Map<String, ISelfDefinedEdit> ret = selfDefinedEdit.get(resourceName);
        if (ret == null) {
            log.error("资源:{}不存在 获取自定义修改实现类", resourceName);
            throw new FactoryException("interface.not.exist.error");
        }
        return ret;
    }


    /**
     * 通过资源类别，获取资源数据查询实现类
     *
     * @param resourceName 资源名称
     * @return 资源数据查询实现类
     */
    public IFileManagerComponent getFileManagerComponent(String resourceName) {

        if (StringUtil.isEmpty(resourceName)) {
            log.error("resource name is null");
            throw new ParamErrorException();
        }
        if (!fileManagerComponentMap.containsKey(resourceName)) {
            throw new FactoryException("interface.not.exist.error");
        }
        log.debug("获取资源：{}文件管理接口", resourceName);
        return fileManagerComponentMap.get(resourceName);
    }
    //end 自定义查询接口初始化
}