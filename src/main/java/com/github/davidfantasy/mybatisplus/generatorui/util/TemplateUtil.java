package com.github.davidfantasy.mybatisplus.generatorui.util;

import java.io.InputStream;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

public class TemplateUtil {

    public static InputStream getBuiltInTemplate(String fileType) {
        //原来是直接读取mybatis-plus-generator中的模板，现在改为读取项目资源目录下的模板
        //InputStream templateIn = AutoGenerator.class.getResourceAsStream("/templates/" + fileType2TemplateName(fileType));
        return TemplateUtil.class.getResourceAsStream("/codetpls/" + fileType2TemplateName(fileType));
    }

    public static String fileType2TemplateName(String fileType) {
        if (fileType.equalsIgnoreCase(FILE_TYPE_MAPPER_XML)
                || fileType.equalsIgnoreCase(FILE_TYPE_MAPPER)) {
            return fileType.toLowerCase() + ".btl";
        }
        if (fileType.equals(FILE_TYPE_SERVICEIMPL)) {
            return "serviceImpl.java.btl";
        }
        return fileType.toLowerCase() + ".java.btl";
    }

}
