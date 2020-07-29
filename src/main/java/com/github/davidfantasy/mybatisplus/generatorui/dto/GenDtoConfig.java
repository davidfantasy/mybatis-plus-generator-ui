package com.github.davidfantasy.mybatisplus.generatorui.dto;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 通过SQL语句创建DTO对象的模板参数类
 */
@Data
public class GenDtoConfig {

    /**
     * 是否解析SQL中的动态参数部分
     */
    private Boolean enableParseDynamicParams;

    private String fullPackage;

    private Boolean enableLombok = false;

    private String comment;

    private String author;

    private String createDate;

    private String mapperLocation;

    private String mapperLocationPrefix;

    private List<String> importPackages = Lists.newArrayList();

    private List<SelectResultField> fields;

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

    public void addImportPackage(String pkg) {
        if (Strings.isNullOrEmpty(pkg)) {
            return;
        }
        if (importPackages == null) {
            importPackages = Lists.newArrayList();
        }
        if (!importPackages.contains(pkg)) {
            importPackages.add(pkg);
        }
    }


}
