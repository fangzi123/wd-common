package com.wdcloud.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;

/**
 * lombok 插件
 */
public class LombokPlugin extends PluginAdapter {
    private boolean equalsAndHashCode = false;
    private boolean toString = false;
    private boolean getter;
    private boolean setter;
    private boolean data;

    public LombokPlugin() {
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }


    @Override
    public void setProperties(Properties properties) {
        this.toString = Boolean.valueOf(properties.getProperty("toString"));
        this.equalsAndHashCode = Boolean.valueOf(properties.getProperty("equalsAndHashCode"));
        this.getter = Boolean.valueOf(properties.getProperty("getter"));
        this.setter = Boolean.valueOf(properties.getProperty("setter"));
        this.data = Boolean.valueOf(properties.getProperty("data"));
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !setter && !data;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !getter && !data;
    }

    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("lombok.*");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        if (data) {
            topLevelClass.addAnnotation("@Data");
        }
        if (getter) {
            topLevelClass.addAnnotation("@Getter");
        }
        if (setter) {
            topLevelClass.addAnnotation("@Setter");
        }
        if (toString) {
            topLevelClass.addAnnotation("@ToString");
        }
        if (equalsAndHashCode) {
            topLevelClass.addAnnotation("@EqualsAndHashCode");
        }
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

}
