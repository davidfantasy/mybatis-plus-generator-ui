package com.github.davidfantasy.mybatisplus.generatorui.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class SelectResultField {

    private String columnName;

    private String javaType;

    private String propertyName;

    public String getGetMethodName() {
        String prefix = "get";
        if ("boolean".equalsIgnoreCase(javaType)) {
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

}
