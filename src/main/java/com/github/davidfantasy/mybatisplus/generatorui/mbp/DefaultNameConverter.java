package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import cn.hutool.core.util.StrUtil;
import com.github.davidfantasy.mybatisplus.generatorui.dto.Constant;
import com.google.common.base.Strings;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_JAVA;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_XML;

public class DefaultNameConverter implements NameConverter {

    @Override
    public String entityNameConvert(String tableName) {
        if (Strings.isNullOrEmpty(tableName)) {
            return "";
        }
        tableName = tableName.substring(tableName.indexOf(StrUtil.UNDERLINE) + 1, tableName.length());
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase()));
    }

    @Override
    public String propertyNameConvert(String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            return "";
        }
        return StrUtil.toCamelCase(fieldName.toLowerCase());
    }

    @Override
    public String outputFileNameConvert(String fileType, String entityName) {
        if (fileType.equals(Constant.FILE_TYPE_ENTITY)) {
            return entityName + DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_MAPPER)) {
            return entityName + "Mapper" + DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_MAPPER_XML)) {
            return entityName + "Mapper" + DOT_XML;
        } else if (fileType.equals(Constant.FILE_TYPE_SERVICE)) {
            return "I" + entityName + "Service" + DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_SERVICEIMPL)) {
            return entityName + "ServiceImpl" + DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_CONTROLLER)) {
            return entityName + "Controller" + DOT_JAVA;
        }
        return entityName + fileType;
    }
}
