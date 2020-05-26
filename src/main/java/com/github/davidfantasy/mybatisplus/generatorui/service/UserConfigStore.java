package com.github.davidfantasy.mybatisplus.generatorui.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.UserConfig;
import com.github.davidfantasy.mybatisplus.generatorui.util.JsonUtil;
import com.github.davidfantasy.mybatisplus.generatorui.util.PathUtil;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Component
@Slf4j
public class UserConfigStore {

    private String storeDir;

    private String userConfigPath;

    @Autowired
    private OutputFileInfoService outputFileInfoService;

    @Autowired
    private GeneratorConfig generatorConfig;

    @PostConstruct
    public void init() {
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
            userConfig.setOutputFiles(outputFileInfoService.getBuiltInFileInfo());
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
            e.printStackTrace();
            log.error("读取用户配置文件发生错误：{}", e.getMessage());
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

}
