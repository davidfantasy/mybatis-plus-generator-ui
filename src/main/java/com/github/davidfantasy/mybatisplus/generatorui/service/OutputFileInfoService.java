package com.github.davidfantasy.mybatisplus.generatorui.service;

import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.OutputFileInfo;
import com.github.davidfantasy.mybatisplus.generatorui.dto.UserConfig;
import com.github.davidfantasy.mybatisplus.generatorui.strategy.*;
import com.github.davidfantasy.mybatisplus.generatorui.util.TemplateUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Service
public class OutputFileInfoService {



    @Autowired
    private UserConfigStore userConfigStore;




    public void deleteOutputFileInfo(OutputFileInfo fileInfo) throws IOException {
        if (fileInfo.isBuiltIn()) {
            throw new ServiceException("内置文件配置信息不能删除");
        }
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        fileInfos.remove(fileInfo);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveOutputFileInfo(OutputFileInfo saveFileInfo) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        //替换原来的配置
        if (saveFileInfo.isBuiltIn()) {
            Collections.replaceAll(fileInfos, saveFileInfo, saveFileInfo);
        } else if (fileInfos.contains(saveFileInfo)) {
            Collections.replaceAll(fileInfos, saveFileInfo, saveFileInfo);
        } else {
            fileInfos.add(saveFileInfo);
        }
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveEntityStrategy(EntityStrategy entityStrategy) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setEntityStrategy(entityStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveMapperXmlStrategy(MapperXmlStrategy mapperXmlStrategy) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setMapperXmlStrategy(mapperXmlStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveMapperStrategy(MapperStrategy mapperStrategy) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setMapperStrategy(mapperStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveControllerStrategy(ControllerStrategy controllerStrategy) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setControllerStrategy(controllerStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveServiceStrategy(ServiceStrategy serviceStrategy) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setServiceStrategy(serviceStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public void saveServiceImplStrategy(ServiceImplStrategy serviceImplStrategy) throws IOException {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        userConfig.setServiceImplStrategy(serviceImplStrategy);
        userConfigStore.saveUserConfig(userConfig);
    }

    public String getOutputPkgByFileType(String fileType) {
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        for (OutputFileInfo fileInfo : fileInfos) {
            if (fileInfo.getFileType().equals(fileType)) {
                return fileInfo.getOutputPackage();
            }
        }
        return null;
    }

}
