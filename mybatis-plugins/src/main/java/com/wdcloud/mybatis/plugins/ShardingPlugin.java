package com.wdcloud.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 功能
 */
public class ShardingPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }


    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("com.wdcloud.model.base.anno.*"));
        Boolean sharding = Boolean.valueOf(introspectedTable.getTableConfiguration().getProperty("sharding"));
        if (sharding) {
            interfaze.addAnnotation("@ShardingTable");
        } else {
            interfaze.addAnnotation("@NoneShardingTable");
        }
        return true;
    }
}
