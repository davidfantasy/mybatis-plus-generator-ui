package com.github.davidfantasy.mybatisplus.generatorui.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.dto.DtoFieldInfo;
import com.github.davidfantasy.mybatisplus.generatorui.dto.JavaClassMethodInfo;
import com.github.davidfantasy.mybatisplus.generatorui.util.PathUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_JAVA;

@Service
public class JavaClassParser {

    private JavaParser jp = new JavaParser();

    @Autowired
    private ProjectPathResolver pathResolver;

    public void addMethod2Interface(JavaClassMethodInfo methodInfo) throws Exception {
        String sourcePath = pathResolver.convertPackageToPath(methodInfo.getClassRef()) + DOT_JAVA;
        FileInputStream in = new FileInputStream(sourcePath);
        ParseResult<CompilationUnit> result = jp.parse(in);
        CompilationUnit cu = result.getResult().get();
        for (String importJavaType : methodInfo.getImportJavaTypes()) {
            cu.addImport(importJavaType);
        }
        String className = PathUtil.getShortNameFromFullRef(methodInfo.getClassRef());
        ClassOrInterfaceDeclaration clazz = cu.getInterfaceByName(className).get();
        NodeList<Parameter> params = new NodeList();
        for (DtoFieldInfo field : methodInfo.getParams()) {
            Parameter param = new Parameter();
            param.setName(field.getPropertyName());
            param.setType(field.getShortJavaType());
            if (field.getAnnotations() != null) {
                param.setAnnotations(field.getAnnotations());
            }
            params.add(param);
        }
        clazz.addMethod(methodInfo.getMethodName())
                .setParameters(params)
                .setBody(null)
                .setType(PathUtil.getShortNameFromFullRef(methodInfo.getReturnType()));
        FileUtil.writeFromStream(IoUtil.toStream(cu.toString(), "utf-8"), sourcePath);
    }

}
