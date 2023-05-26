package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StartsWithMatcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.RESOURCE_PREFIX_CLASSPATH;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.RESOURCE_PREFIX_FILE;

/**
 * 对原模板引擎进行改造，使其支持file和classpath两类加载模式
 */
@Slf4j
public class BeetlTemplateEngine extends AbstractTemplateEngine {

    private GroupTemplate groupTemplate;

    private final String templateStoreDir;

    private final NameConverter nameConverter;

    public BeetlTemplateEngine(NameConverter nameConverter, String templateStoreDir) {
        this.templateStoreDir = templateStoreDir;
        this.nameConverter = nameConverter;
        try {
            log.info("模板根目录为：" + templateStoreDir);
            ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader(this.getClass().getClassLoader());
            FileResourceLoader fileResourceLoader = new FileResourceLoader(templateStoreDir);
            CompositeResourceLoader loader = new CompositeResourceLoader();
            loader.addResourceLoader(new StartsWithMatcher(RESOURCE_PREFIX_CLASSPATH).withoutPrefix(), classpathResourceLoader);
            loader.addResourceLoader(new StartsWithMatcher(RESOURCE_PREFIX_FILE).withoutPrefix(), fileResourceLoader);
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(loader, cfg);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, File outputFile) throws Exception {
        if (templatePath.startsWith(RESOURCE_PREFIX_FILE)) {
            templatePath = templatePath.replace(templateStoreDir, "");
        }
        log.info("templatePath:" + templatePath);
        Template template = groupTemplate.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.binding(objectMap);
            template.renderTo(fileOutputStream);
        }
        log.info("已生成文件:" + outputFile.getPath());
    }

    @Override
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        return this;
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

    @Override
    protected void outputCustomFile(List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();
        String parentPath = getPathInfo(OutputFile.parent);
        customFiles.forEach(file -> {
            String filePath = StrUtil.isNotBlank(file.getFilePath()) ? file.getFilePath() : parentPath;
            if (StrUtil.isNotBlank(file.getPackageName())) {
                filePath = filePath + File.separator + file.getPackageName();
                filePath = filePath.replaceAll("\\.", "\\" + File.separator);
            }
            String fileName = filePath + File.separator + nameConverter.customFileNameConvert(file.getFileName(), entityName);
            outputFile(new File(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }

}
