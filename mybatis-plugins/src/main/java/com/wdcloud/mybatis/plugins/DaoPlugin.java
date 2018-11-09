package com.wdcloud.mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.TableConfiguration;

import java.util.List;
import java.util.Properties;

public class DaoPlugin extends PluginAdapter {

    private String targetProject = "src/main/java";
    private String topLevelClass;
    private String parentClass;

    @Override
    public void setProperties(Properties properties) {
        this.targetProject = ((String) properties.get("targetProject"));
        this.topLevelClass = ((String) properties.get("topLevelClass"));
        this.parentClass = ((String) properties.get("parentClass"));
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        String domainObjectName = tableConfiguration.getDomainObjectName();
        TopLevelClass compilationUnit = new TopLevelClass(topLevelClass + "." + domainObjectName + "Dao");

        List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
        for (GeneratedJavaFile generatedJavaFile : generatedJavaFiles) {
            if (!generatedJavaFile.getCompilationUnit().isJavaInterface()) {
                compilationUnit.addImportedType(generatedJavaFile.getTargetPackage() + "." + domainObjectName);
            }
        }

        FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(parentClass);
        superClass.addTypeArgument(new FullyQualifiedJavaType(domainObjectName));


        IntrospectedColumn primaryKeyColumn = null;
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : allColumns) {
            if (column.isIdentity()) {
                primaryKeyColumn = column;
                break;
            }
        }
        if (primaryKeyColumn == null) {
            throw new RuntimeException("Table primary key is not exists.");
        }
        superClass.addTypeArgument(primaryKeyColumn.getFullyQualifiedJavaType());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("getBeanClass");
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.Class");
        returnType.addTypeArgument(new FullyQualifiedJavaType(domainObjectName));
        method.setReturnType(returnType);
        method.addBodyLine("return " + domainObjectName + ".class;");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Class"));
        compilationUnit.addMethod(method);

        compilationUnit.addImportedType("org.springframework.stereotype.Repository");
        compilationUnit.addAnnotation("@Repository");
        compilationUnit.setSuperClass(superClass);
        compilationUnit.setVisibility(JavaVisibility.PUBLIC);
        compilationUnit.addImportedType(parentClass);
        GeneratedJavaFile javaFile = new GeneratedJavaFile(compilationUnit, targetProject, new DefaultJavaFormatter());
        System.out.println("-------------------------");
        return List.of(javaFile);
    }
}
