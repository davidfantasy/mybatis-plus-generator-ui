package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StartsWithMatcher;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.RESOURCE_PREFIX_CLASSPATH;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.RESOURCE_PREFIX_FILE;

/**
 * 对原模板引擎进行改造，使其支持file和classpath两类加载模式
 */
public class BeetlTemplateEngine extends AbstractTemplateEngine {

    private GroupTemplate groupTemplate;

    private String templateStoreDir;

    public BeetlTemplateEngine(String templateStoreDir) {
        this.templateStoreDir = templateStoreDir;
        try {
            logger.info("模板根目录为：" + templateStoreDir);
            ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader(this.getClass().getClassLoader());
            FileResourceLoader fileResourceLoader = new FileResourceLoader(templateStoreDir);
            CompositeResourceLoader loader = new CompositeResourceLoader();
            loader.addResourceLoader(new StartsWithMatcher(RESOURCE_PREFIX_CLASSPATH).withoutPrefix(), classpathResourceLoader);
            loader.addResourceLoader(new StartsWithMatcher(RESOURCE_PREFIX_FILE).withoutPrefix(), fileResourceLoader);
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(loader, cfg);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (templatePath.startsWith(RESOURCE_PREFIX_FILE)) {
            templatePath = templatePath.replace(templateStoreDir, "");
        }
        logger.info("templatePath:" + templatePath);
        Template template = groupTemplate.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.binding(objectMap);
            template.renderTo(fileOutputStream);
        }
        logger.info("已生成文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath;
    }

    public String write2String(Map<String, Object> objectMap, String templatePath) {
        if (templatePath.startsWith(RESOURCE_PREFIX_FILE)) {
            templatePath = templatePath.replace(templateStoreDir, "");
        }
        Template template = groupTemplate.getTemplate(templatePath);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            template.binding(objectMap);
            template.renderTo(baos);
            return baos.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
