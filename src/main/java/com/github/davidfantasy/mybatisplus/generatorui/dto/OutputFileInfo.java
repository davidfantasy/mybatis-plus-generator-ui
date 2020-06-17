package com.github.davidfantasy.mybatisplus.generatorui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import lombok.Data;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputFileInfo {
    /**
     * 输出文件信息索引
     */
    private Long index;

    private String fileType;

    /**
     * 文件的输出包名
     */
    private String outputLocation;

    private String templateName;

    private String templatePath;
    /**
     * 是否内置
     */
    private boolean builtIn;

    @JsonIgnore
    public String getAvailableTemplatePath() {
        if (!Strings.isNullOrEmpty(templatePath)) {
            return templatePath;
        }
        if (fileType.equals(FILE_TYPE_ENTITY)) {
            return RESOURCE_PREFIX_CLASSPATH + "/templates/entity.java.btl";
        } else if (fileType.equals(FILE_TYPE_MAPPER)) {
            return RESOURCE_PREFIX_CLASSPATH + "/templates/mapper.java.btl";
        } else if (fileType.equals(FILE_TYPE_MAPPER_XML)) {
            return RESOURCE_PREFIX_CLASSPATH + "/templates/mapper.xml.btl";
        } else if (fileType.equals(FILE_TYPE_SERVICE)) {
            return RESOURCE_PREFIX_CLASSPATH + "/templates/service.java.btl";
        } else if (fileType.equals(FILE_TYPE_SERVICEIMPL)) {
            return RESOURCE_PREFIX_CLASSPATH + "/templates/serviceImpl.java.btl";
        } else if (fileType.equals(FILE_TYPE_CONTROLLER)) {
            return RESOURCE_PREFIX_CLASSPATH + "/templates/controller.java.btl";
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
