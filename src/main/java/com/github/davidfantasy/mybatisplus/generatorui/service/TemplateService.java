package com.github.davidfantasy.mybatisplus.generatorui.service;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.FILE_TYPE_MAPPER;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.FILE_TYPE_MAPPER_XML;

@Service
public class TemplateService {

    public InputStream getBuiltInTemplate(String fileType) {
        InputStream templateIn = AutoGenerator.class.getResourceAsStream("/templates/" + fileType2TemplateName(fileType));
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
