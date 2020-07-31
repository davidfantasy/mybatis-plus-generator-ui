package com.github.davidfantasy.mybatisplus.generatorui.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.FILE_TYPE_MAPPER;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.FILE_TYPE_MAPPER_XML;

@Service
public class TemplateService {

    public InputStream getBuiltInTemplate(String fileType) {
        //原来是直接读取mybatis-plus-generator中的模板，现在改为读取项目资源目录下的模板
        //InputStream templateIn = AutoGenerator.class.getResourceAsStream("/templates/" + fileType2TemplateName(fileType));
        InputStream templateIn = this.getClass().getResourceAsStream("/codetpls/" + fileType2TemplateName(fileType));
        return templateIn;
    }

    public String fileType2TemplateName(String fileType) {
        if (fileType.equalsIgnoreCase(FILE_TYPE_MAPPER_XML)
                || fileType.equalsIgnoreCase(FILE_TYPE_MAPPER)) {
            return fileType.toLowerCase() + ".btl";
        }
        return fileType.toLowerCase() + ".java.btl";
    }

}
