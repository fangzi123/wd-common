package com.wdcloud.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityHelperPlugin extends PluginAdapter {


    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn allColumn : allColumns) {
            String columnName = allColumn.getActualColumnName();

            Field field = buildField(columnName.toUpperCase(), underline2Camel(columnName));
            topLevelClass.addField(field);
        }
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    private Field buildField(String name, String val) {
        Field myField = new Field();
        myField.setName(name);
        myField.setInitializationString("\"" + val + "\"");
        myField.setFinal(true);
        myField.setStatic(true);
        myField.setVisibility(JavaVisibility.PUBLIC);
        myField.setType(FullyQualifiedJavaType.getStringInstance());

        return myField;
    }

    private String underline2Camel(String line, boolean ... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if((smallCamel.length ==0 || smallCamel[0] ) && matcher.start()==0){
                sb.append(Character.toLowerCase(word.charAt(0)));
            }else{
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
}
