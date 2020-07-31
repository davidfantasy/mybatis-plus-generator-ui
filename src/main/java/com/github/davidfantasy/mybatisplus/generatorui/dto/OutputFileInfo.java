package com.github.davidfantasy.mybatisplus.generatorui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.google.common.base.Strings;
import lombok.Data;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFileInfo {

    private String fileType;

    /**
     * 文件的输出包名
     */
    private String outputLocation;

    private String templateName;

    private String templatePath;

    private boolean builtIn;

    public String getOutputPackage() {
        if (Strings.isNullOrEmpty(outputLocation)) {
            return "";
        }
        if (outputLocation.startsWith(PACKAGE_RESOURCES_PREFIX)) {
            return outputLocation.replaceFirst(PACKAGE_RESOURCES_PREFIX, "");
        } else if (outputLocation.startsWith(PACKAGE_JAVA_PREFIX)) {
            return outputLocation.replaceFirst(PACKAGE_JAVA_PREFIX, "");
        }
        return outputLocation;
    }

    @JsonIgnore
    public String getAvailableTemplatePath() {
        if (!Strings.isNullOrEmpty(templatePath)) {
            return templatePath;
        }
        if (fileType.equals(FILE_TYPE_ENTITY)) {
            return RESOURCE_PREFIX_CLASSPATH + "codetpls/entity.java.btl";
        } else if (fileType.equals(FILE_TYPE_MAPPER)) {
            return RESOURCE_PREFIX_CLASSPATH + "codetpls/mapper.java.btl";
        } else if (fileType.equals(FILE_TYPE_MAPPER_XML)) {
            return RESOURCE_PREFIX_CLASSPATH + "codetpls/mapper.xml.btl";
        } else if (fileType.equals(FILE_TYPE_SERVICE)) {
            return RESOURCE_PREFIX_CLASSPATH + "codetpls/service.java.btl";
        } else if (fileType.equals(FILE_TYPE_SERVICEIMPL)) {
            return RESOURCE_PREFIX_CLASSPATH + "codetpls/serviceImpl.java.btl";
        } else if (fileType.equals(FILE_TYPE_CONTROLLER)) {
            return RESOURCE_PREFIX_CLASSPATH + "codetpls/controller.java.btl";
        }
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OutputFileInfo) {
            OutputFileInfo file = (OutputFileInfo) obj;
            if (file.getFileType() == null || this.getFileType() == null) {
                return false;
            }
            return file.getFileType().equalsIgnoreCase(this.getFileType());
        }
        return false;
    }

}
