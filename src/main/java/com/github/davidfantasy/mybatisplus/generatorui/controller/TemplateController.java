package com.github.davidfantasy.mybatisplus.generatorui.controller;

import cn.hutool.core.io.IoUtil;
import com.github.davidfantasy.mybatisplus.generatorui.common.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.OutputFileInfo;
import com.github.davidfantasy.mybatisplus.generatorui.dto.UserConfig;
import com.github.davidfantasy.mybatisplus.generatorui.service.OutputFileInfoService;
import com.github.davidfantasy.mybatisplus.generatorui.service.UserConfigStore;
import com.github.davidfantasy.mybatisplus.generatorui.util.TemplateUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/template")
@Slf4j
public class TemplateController {

    @Autowired
    private UserConfigStore userConfigStore;


    @Autowired
    private OutputFileInfoService outputFileInfoService;

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileType) throws IOException {
        if (Strings.isNullOrEmpty(fileType)) {
            log.error("fileType不能为空");
            return ResponseEntity.badRequest().build();
        }
        UserConfig userConfig = userConfigStore.getUserConfigFromFile();
        if (userConfig == null) {
            InputStream tplIn = TemplateUtil.getBuiltInTemplate(fileType);
            return toDownloadEntity(tplIn);
        }
        List<OutputFileInfo> fileInfos = userConfig.getOutputFiles();
        for (OutputFileInfo fileInfo : fileInfos) {
            if (fileType.equals(fileInfo.getFileType())) {
                if (fileInfo.isBuiltIn()
                        && Strings.isNullOrEmpty(fileInfo.getTemplatePath())) {
                    InputStream tplIn = TemplateUtil.getBuiltInTemplate(fileType);
                    return toDownloadEntity(tplIn);
                } else {
                    String tplPath = fileInfo.getTemplatePath();
                    if (tplPath.startsWith("file:")) {
                        tplPath = tplPath.replaceFirst("file:", "");
                    }
                    File tplFile = new File(tplPath);
                    if (tplFile.exists()) {
                        return toDownloadEntity(Files.newInputStream(tplFile.toPath()));
                    } else {
                        throw new ServiceException("未找到模板文件：" + fileInfo.getTemplatePath());
                    }
                }
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType) {
        Map<String, Object> params = Maps.newHashMap();
        String storePath = userConfigStore.uploadTemplate(file);
        params.put("templatePath", storePath);
        params.put("templateName", file.getOriginalFilename());
        return ResultGenerator.genSuccessResult(params);
    }

    /**
     * 从输入流构建http响应
     * @param tplIn 流
     * @return http响应
     */
    private ResponseEntity<byte[]> toDownloadEntity(InputStream tplIn) {
        if (tplIn == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(IoUtil.readBytes(tplIn));


    }
}
