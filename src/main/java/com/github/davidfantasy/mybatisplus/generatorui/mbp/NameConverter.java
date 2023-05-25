package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import cn.hutool.core.util.StrUtil;
import com.github.davidfantasy.mybatisplus.generatorui.dto.Constant;
import com.google.common.base.Strings;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_JAVA;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_XML;

/**
 * 自定义各类名称转换的规则
 */
public interface NameConverter {

    /**
     * 自定义Entity.java的类名称（文件名等于类名加固定后缀）
     *
     * @param tableName 表名称
     * @return 转换后的实体类名称
     */
    default String entityNameConvert(String tableName, String tablePrefix) {
        if (Strings.isNullOrEmpty(tableName)) {
            return "";
        }
        if (!StrUtil.isBlank(tablePrefix) && tableName.startsWith(tablePrefix)) {
            tableName = tableName.substring(tablePrefix.length());
        }
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase()));
    }

    /**
     * 自定义表字段名到实体类属性名的转换规则
     *
     * @param fieldName 表字段名称
     * @return 转换的后属性名称
     */
    default String propertyNameConvert(String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            return "";
        }
        if (fieldName.contains("_")) {
            return StrUtil.toCamelCase(fieldName.toLowerCase());
        }
        return fieldName;
    }

    /**
     * 自定义Mapper.java的类名称（文件名等于类名加固定后缀）
     */
    default String mapperNameConvert(String entityName) {
        return entityName + "Mapper";
    }

    /**
     * 自定义Mapper.xml的文件名称（无需添加后缀名）
     */
    default String mapperXmlNameConvert(String entityName) {
        return entityName + "Mapper";
    }

    /**
     * 自定义Service.java的类名称（文件名等于类名加固定后缀）
     */
    default String serviceNameConvert(String entityName) {
        return "I" + entityName + "Service";
    }

    /**
     * 自定义ServiceImpl.java的类名称（文件名等于类名加固定后缀）
     */
    default String serviceImplNameConvert(String entityName) {
        return entityName + "ServiceImpl";
    }

    /**
     * 自定义Controller.java的类名称（文件名等于类名加固定后缀）
     */
    default String controllerNameConvert(String entityName) {
        return entityName + "Controller";
    }

    /**
     * 用户自定义生成文件的文件名
     *
     * @param fileType   用户自定义的文件类型
     * @param entityName 数据表关联的entity的名称
     * @return 生成的文件的文件名，需要添加必要的文件后缀
     */
    default String customFileNameConvert(String fileType, String entityName) {
        return entityName + fileType;
    }

}
