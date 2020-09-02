package com.github.davidfantasy.mybatisplus.generatorui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 通过SQL语句创建DTO对象的模板参数类
 */
@Data
public class GenDtoConfig {

    /**
     * 是否解析SQL中的动态参数部分
     */
    private boolean enableParseDynamicParams;

    private String fullPackage;

    private boolean enableLombok;

    /**
     * 是否为分页查询
     */
    private boolean enablePageQuery;

    private String comment;

    private String author;

    private String createDate;

    private String mapperMethod;

    private String mapperLocation;

    private String mapperLocationPrefix;

    private Boolean enableCreateDaoMethod;

    private String daoMethodParamType;

    private String daoMethodParamDto;

    /**
     * 是否自动创建映射查询的结果的DTO
     */
    private boolean autoCreatedResultDto;

    private Set<String> importPackages = Sets.newHashSet();

    @JsonIgnore
    private List<DtoFieldInfo> fields;

    public String getPkg() {
        if (Strings.isNullOrEmpty(fullPackage)) {
            return "";
        }
        return fullPackage.substring(0, fullPackage.lastIndexOf("."));
    }

    public String getDtoName() {
        if (Strings.isNullOrEmpty(fullPackage)) {
            return "";
        }
        return fullPackage.substring(fullPackage.lastIndexOf(".") + 1, fullPackage.length());
    }

    public String getMapperElementId() {
        if (Strings.isNullOrEmpty(mapperLocation)) {
            return "";
        }
        return mapperLocation.substring(mapperLocation.lastIndexOf(".") + 1, mapperLocation.length());
    }

    public String getMapperPackage() {
        if (Strings.isNullOrEmpty(mapperLocation)) {
            return "";
        }
        String pkg = mapperLocation.substring(0, mapperLocation.lastIndexOf("."));
        if (!Strings.isNullOrEmpty(mapperLocationPrefix) && !"java".equals("mapperLocationPrefix")) {
            pkg = mapperLocationPrefix + ":" + pkg;
        }
        return pkg;
    }

    public String getResultType() {
        if (Strings.isNullOrEmpty(this.getFullPackage())) {
            return "java.util.Map";
        }
        return this.getFullPackage();
    }

    public String getResultMap() {
        if (!Strings.isNullOrEmpty(this.getFullPackage()) && autoCreatedResultDto) {
            if (this.getFields() == null) {
                return null;
            }
            //判断字段名是否和属性名一致，全部一致的情况无需生成resultMap
            for (DtoFieldInfo fieldInfo : this.getFields()) {
                if (!fieldInfo.getPropertyName().equals(fieldInfo.getColumnName())) {
                    return this.getDtoName() + "Map";
                }
            }
        }
        return null;
    }


}
