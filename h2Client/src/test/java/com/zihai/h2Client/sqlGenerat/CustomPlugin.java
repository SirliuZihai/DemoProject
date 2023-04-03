package com.zihai.h2Client.sqlGenerat;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.InsertSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;

import java.util.List;

/**
 * 自定义插件
 * 增加
 * 1. find
 * 2. list
 * 3. pageList
 * 
 * 参考
 * @see org.mybatis.generator.plugins.ToStringPlugin
 * @author My
 */
public class CustomPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        AbstractXmlElementGenerator elementGenerator = new CustomAbstractXmlElementGenerator();
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(document.getRootElement());

        /* AbstractXmlElementGenerator elementGenerator1 = new InsertElementGenerator(true);*/
        AbstractXmlElementGenerator elementGenerator1 = new InsertSelectiveElementGenerator();
        elementGenerator1.setContext(context);
        elementGenerator1.setIntrospectedTable(introspectedTable);
        elementGenerator1.addElements(document.getRootElement());


      /*  AbstractXmlElementGenerator elementGenerator3  = new UpdateByPrimaryKeySelectiveElementGenerator();
        elementGenerator3.setContext(context);
        elementGenerator3.setIntrospectedTable(introspectedTable);
        elementGenerator3.addElements(document.getRootElement());*/
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        AbstractJavaMapperMethodGenerator methodGenerator = new CustomJavaMapperMethodGenerator();
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.addInterfaceElements(interfaze);

        //AbstractJavaMapperMethodGenerator methodGenerator1 = new InsertMethodGenerator(false);
        AbstractJavaMapperMethodGenerator methodGenerator1 = new InsertSelectiveMethodGenerator();
        methodGenerator1.setContext(context);
        methodGenerator1.setIntrospectedTable(introspectedTable);
        methodGenerator1.addInterfaceElements(interfaze);

        /*AbstractJavaMapperMethodGenerator methodGenerator3 = new UpdateByPrimaryKeySelectiveMethodGenerator();
        methodGenerator3.setContext(context);
        methodGenerator3.setIntrospectedTable(introspectedTable);
        methodGenerator3.addInterfaceElements(interfaze);*/
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

}