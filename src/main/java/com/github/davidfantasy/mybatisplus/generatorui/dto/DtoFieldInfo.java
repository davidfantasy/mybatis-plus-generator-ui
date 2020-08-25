package com.github.davidfantasy.mybatisplus.generatorui.dto;

import cn.hutool.core.util.StrUtil;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DtoFieldInfo {

    private Set<String> importJavaTypes = Sets.newHashSet();

    private String columnName;

    private String shortJavaType;

    private String propertyName;

    private NodeList<AnnotationExpr> annotations;

    public String getGetMethodName() {
        String prefix = "get";
        if ("boolean".equalsIgnoreCase(shortJavaType)) {
            prefix = "is";
        }
        return prefix + StrUtil.upperFirst(propertyName);
    }

    public String getSetMethodName() {
        return "set" + StrUtil.upperFirst(propertyName);
    }

    public Boolean isConverted() {
        return columnName.equals(propertyName);
    }

    public void addImportJavaType(String type) {
        if (!Strings.isNullOrEmpty(type)) {
            importJavaTypes.add(type);
        }
    }

}
