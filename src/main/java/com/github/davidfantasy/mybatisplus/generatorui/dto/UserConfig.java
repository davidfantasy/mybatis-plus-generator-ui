package com.github.davidfantasy.mybatisplus.generatorui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.davidfantasy.mybatisplus.generatorui.strategy.*;
import lombok.Data;

import java.util.List;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserConfig {

    private List<OutputFileInfo> outputFiles;

    private EntityStrategy entityStrategy = new EntityStrategy();

    private MapperStrategy mapperStrategy = new MapperStrategy();

    private MapperXmlStrategy mapperXmlStrategy = new MapperXmlStrategy();

    private ControllerStrategy controllerStrategy = new ControllerStrategy();

    private ServiceStrategy serviceStrategy = new ServiceStrategy();

    private ServiceImplStrategy serviceImplStrategy = new ServiceImplStrategy();

    @JsonIgnore
    public OutputFileInfo getControllerInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_CONTROLLER.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getEntityInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_ENTITY.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getMapperInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_MAPPER.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getMapperXmlInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_MAPPER_XML.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getServiceInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_SERVICE.equals(f.getFileType()))).findFirst().get();
    }

    @JsonIgnore
    public OutputFileInfo getServiceImplInfo() {
        if (outputFiles == null) {
            return null;
        }
        return outputFiles.stream().filter((f -> FILE_TYPE_SERVICEIMPL.equals(f.getFileType()))).findFirst().get();
    }

}
