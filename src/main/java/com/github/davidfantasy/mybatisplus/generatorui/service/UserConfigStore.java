package com.github.davidfantasy.mybatisplus.generatorui.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.OutputFileInfo;
import com.github.davidfantasy.mybatisplus.generatorui.dto.UserConfig;
import com.github.davidfantasy.mybatisplus.generatorui.util.JsonUtil;
import com.github.davidfantasy.mybatisplus.generatorui.util.PathUtil;
import com.github.davidfantasy.mybatisplus.generatorui.util.TemplateUtil;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Component
@Slf4j
public class UserConfigStore implements InitializingBean {

    private String storeDir;

    private String userConfigPath;

    @Autowired
    private ProjectPathResolver pathResolver;

    @Autowired
    private GeneratorConfig generatorConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        this.storeDir = PathUtil.joinPath(System.getProperty("user.home"), CONFIG_HOME, generatorConfig.getBasePackage());
        this.userConfigPath = this.storeDir + File.separator + "user-config.json";
    }

    public String getTemplateStoreDir() {
        return PathUtil.joinPath(this.storeDir, TEMPLATE_STORE_DIR);
    }

    public UserConfig getDefaultUserConfig() {
        UserConfig userConfig = getUserConfigFromFile();
        if (userConfig == null) {
            userConfig = new UserConfig();
            userConfig.setOutputFiles(getBuiltInFileInfo());
        }
        return userConfig;
    }

    public UserConfig getUserConfigFromFile() {
        if (!FileUtil.exist(this.userConfigPath)) {
            return null;
        }
        String userConfigStr = FileUtil.readString(userConfigPath, Charset.forName("utf-8"));
        try {
            return JsonUtil.json2obj(userConfigStr, UserConfig.class);
        } catch (Exception e) {
            log.error("读取用户配置文件发生错误：", e);
            return null;
        }
    }

    public void saveUserConfig(UserConfig userConfig) throws IOException {
        if (userConfig == null) {
            throw new ServiceException("不能写入空的用户配置");
        }
        String configStr = JsonUtil.obj2json(userConfig);
        File userConfigFile = new File(this.userConfigPath);
        if (userConfigFile.exists()) {
            userConfigFile.delete();
        }
        Files.createParentDirs(userConfigFile);
        userConfigFile.createNewFile();
        FileUtil.writeFromStream(new ByteArrayInputStream(configStr.getBytes(Charset.forName("utf-8"))), userConfigFile);
    }

    public String uploadTemplate(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileSuffix = fileName.substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String saveFileName = fileName.substring(0, fileName.lastIndexOf(fileSuffix)) + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String savePath = PathUtil.joinPath(getTemplateStoreDir(), saveFileName);
        log.info("模板上传路径为：{}", savePath);
        File saveFile = new File(savePath);
        try {
            FileUtil.writeFromStream(file.getInputStream(), saveFile);
        } catch (IOException e) {
            throw new ServiceException("上传模板文件失败", e);
        }
        return RESOURCE_PREFIX_FILE + savePath;
    }

    public boolean checkUserConfigExisted() {
        if (!FileUtil.exist(this.storeDir)) {
            return false;
        }
        return true;
    }

    public void importProjectConfig(String sourcePkg) throws IOException {
        String configHomePath = PathUtil.joinPath(System.getProperty("user.home"), CONFIG_HOME);
        if (!FileUtil.exist(configHomePath)) {
            throw new ServiceException("配置主目录不存在：" + configHomePath);
        }
        File[] files = FileUtil.ls(configHomePath);
        boolean flag = false;
        for (File file : files) {
            if (file.isDirectory() && file.getName().equals(sourcePkg)) {
                File projectConfigDir = new File(this.storeDir);
                FileUtil.copyContent(file, projectConfigDir, true);
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new ServiceException("未找到待导入的源项目配置");
        }
        String sourceProjectConfigPath = PathUtil.joinPath(System.getProperty("user.home"), CONFIG_HOME, sourcePkg);
        String targetProjectConfigPath = this.storeDir;
        UserConfig currentUserConfig = new UserConfig();
        currentUserConfig.setOutputFiles(getBuiltInFileInfo());
        currentUserConfig.merge(this.getUserConfigFromFile(), sourceProjectConfigPath, targetProjectConfigPath);
        this.saveUserConfig(currentUserConfig);
    }

    public List<String> getAllSavedProject() {
        String configHomePath = PathUtil.joinPath(System.getProperty("user.home"), CONFIG_HOME);
        if (!FileUtil.exist(configHomePath)) {
            return Collections.emptyList();
        }
        List<String> projects = Lists.newArrayList();
        File[] files = FileUtil.ls(configHomePath);
        for (File file : files) {
            if (file.isDirectory()) {
                projects.add(file.getName());
            }
        }
        return projects;
    }

    /**
     * 默认的内置输出文件的信息
     */
    private List<OutputFileInfo> getBuiltInFileInfo() {
        List<OutputFileInfo> builtInFiles = Lists.newArrayList();
        //Entity
        OutputFileInfo entityFile = new OutputFileInfo();
        entityFile.setBuiltIn(true);
        entityFile.setFileType(FILE_TYPE_ENTITY);
        entityFile.setOutputLocation(pathResolver.resolveEntityPackage());
        entityFile.setTemplateName(TemplateUtil.fileType2TemplateName(entityFile.getFileType()));
        builtInFiles.add(entityFile);
        //Mapper xml
        OutputFileInfo mapperXmlFile = new OutputFileInfo();
        mapperXmlFile.setBuiltIn(true);
        mapperXmlFile.setFileType(FILE_TYPE_MAPPER_XML);
        mapperXmlFile.setOutputLocation(pathResolver.resolveMapperXmlPackage());
        mapperXmlFile.setTemplateName(TemplateUtil.fileType2TemplateName(mapperXmlFile.getFileType()));
        builtInFiles.add(mapperXmlFile);
        //Mapper
        OutputFileInfo mapperFile = new OutputFileInfo();
        mapperFile.setBuiltIn(true);
        mapperFile.setFileType(FILE_TYPE_MAPPER);
        mapperFile.setOutputLocation(pathResolver.resolveMapperPackage());
        mapperFile.setTemplateName(TemplateUtil.fileType2TemplateName(mapperFile.getFileType()));
        builtInFiles.add(mapperFile);
        //Service
        OutputFileInfo serviceFile = new OutputFileInfo();
        serviceFile.setBuiltIn(true);
        serviceFile.setFileType(FILE_TYPE_SERVICE);
        serviceFile.setOutputLocation(pathResolver.resolveServicePackage());
        serviceFile.setTemplateName(TemplateUtil.fileType2TemplateName(serviceFile.getFileType()));
        builtInFiles.add(serviceFile);
        //Service Impl
        OutputFileInfo serviceImplFile = new OutputFileInfo();
        serviceImplFile.setBuiltIn(true);
        serviceImplFile.setFileType(FILE_TYPE_SERVICEIMPL);
        serviceImplFile.setOutputLocation(pathResolver.resolveServiceImplPackage());
        serviceImplFile.setTemplateName(TemplateUtil.fileType2TemplateName(serviceImplFile.getFileType()));
        builtInFiles.add(serviceImplFile);
        //Controller
        OutputFileInfo controllerFile = new OutputFileInfo();
        controllerFile.setBuiltIn(true);
        controllerFile.setFileType(FILE_TYPE_CONTROLLER);
        controllerFile.setOutputLocation(pathResolver.resolveControllerPackage());
        controllerFile.setTemplateName(TemplateUtil.fileType2TemplateName(controllerFile.getFileType()));
        builtInFiles.add(controllerFile);
        return builtInFiles;
    }

}
