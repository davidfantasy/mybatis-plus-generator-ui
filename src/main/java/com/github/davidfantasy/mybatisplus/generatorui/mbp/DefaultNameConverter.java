package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.davidfantasy.mybatisplus.generatorui.dto.Constant;
import com.google.common.base.Strings;

public class DefaultNameConverter implements NameConverter {

    @Override
    public String entityNameConvert(String tableName) {
        if (Strings.isNullOrEmpty(tableName)) {
            return "";
        }
        tableName = tableName.substring(tableName.indexOf("_") + 1, tableName.length());
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
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
            return entityName + StringPool.DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_MAPPER)) {
            return entityName + "Mapper" + StringPool.DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_MAPPER_XML)) {
            return entityName + "Mapper" + StringPool.DOT_XML;
        } else if (fileType.equals(Constant.FILE_TYPE_SERVICE)) {
            return "I" + entityName + "Service" + StringPool.DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_SERVICEIMPL)) {
            return entityName + "ServiceImpl" + StringPool.DOT_JAVA;
        } else if (fileType.equals(Constant.FILE_TYPE_CONTROLLER)) {
            return entityName + "Controller" + StringPool.DOT_JAVA;
        }
        return entityName + fileType;
    }
}
