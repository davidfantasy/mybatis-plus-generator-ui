package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.Constant;
import com.github.davidfantasy.mybatisplus.generatorui.dto.GenSetting;
import com.github.davidfantasy.mybatisplus.generatorui.dto.OutputFileInfo;
import com.github.davidfantasy.mybatisplus.generatorui.dto.UserConfig;
import com.github.davidfantasy.mybatisplus.generatorui.service.UserConfigStore;
import com.github.davidfantasy.mybatisplus.generatorui.strategy.*;
import com.github.davidfantasy.mybatisplus.generatorui.util.PathUtil;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
@Slf4j
@AllArgsConstructor
public class MbpGenerator {

    @Autowired
    private DataSourceConfig ds;

    @Autowired
    private GeneratorConfig generatorConfig;

    @Autowired
    private UserConfigStore userConfigStore;

    @Autowired
    private ProjectPathResolver projectPathResolver;

    @Autowired
    private BeetlTemplateEngine beetlTemplateEngine;

    /**
     * 根据所选择的配置生成CRUD代码
     */
    public void genCodeBatch(GenSetting genSetting, List<String> tables) {
        checkGenSetting(genSetting);
        projectPathResolver.refreshBaseProjectPath(genSetting.getRootPath());
        //生成策略配置
        UserConfig userConfig = userConfigStore.getDefaultUserConfig();
        FastAutoGenerator
                .create(ds.getUrl(), ds.getUsername(), ds.getPassword())
                .dataSourceConfig(
                        builder -> {
                            builder.schema(generatorConfig.getSchemaName());
                            builder.typeConvert(generatorConfig.getTypeConvert());
                        }
                ).globalConfig(builder -> {
                    builder.dateType(generatorConfig.getDateType());
                    //指定所有生成文件的根目录
                    builder.outputDir(projectPathResolver.getSourcePath());
                    builder.author(genSetting.getAuthor());
                    if (userConfig.getEntityStrategy().isSwagger2()) {
                        builder.enableSwagger();
                    }
                }).templateEngine(beetlTemplateEngine).packageConfig(builder -> {
                    configPackage(builder, genSetting.getModuleName(), userConfig);
                }).templateConfig(builder -> {
                    configTemplate(builder, genSetting.getChoosedOutputFiles(), userConfig);
                }).injectionConfig(builder -> {
                    configInjection(builder, userConfig, genSetting);
                }).strategyConfig(builder -> {
                    builder.addInclude(String.join(",", tables))
                            .disableSqlFilter()
                            .enableSkipView();
                    configEntity(builder.entityBuilder(), userConfig.getEntityStrategy(), genSetting.isOverride());
                    configMapper(builder.mapperBuilder(), userConfig.getMapperStrategy(), userConfig.getMapperXmlStrategy(), genSetting.isOverride());
                    configService(builder.serviceBuilder(), userConfig.getServiceStrategy(), userConfig.getServiceImplStrategy(), genSetting.isOverride());
                    configController(builder.controllerBuilder(), userConfig.getControllerStrategy(), genSetting.isOverride());
                }).execute();
    }

    private void configPackage(PackageConfig.Builder builder, String moduleName, UserConfig userConfig) {
        String mapperXmlOutputPath = getOutputPathByFileType(Constant.FILE_TYPE_MAPPER_XML, userConfig);
        if (!StrUtil.isEmpty(moduleName)) {
            mapperXmlOutputPath = mapperXmlOutputPath + File.separator + moduleName;
        }
        //这里的模块名处理方式和原版的MPG不同，是将模块名放在包名最后
        String entityPkg = PathUtil.joinPackage(userConfig.getEntityInfo().getOutputPackage(), moduleName);
        String mapperPkg = PathUtil.joinPackage(userConfig.getMapperInfo().getOutputPackage(), moduleName);
        String servicePkg = PathUtil.joinPackage(userConfig.getServiceInfo().getOutputPackage(), moduleName);
        String serviceImplPkg = PathUtil.joinPackage(userConfig.getServiceImplInfo().getOutputPackage(), moduleName);
        String controllerPkg = PathUtil.joinPackage(userConfig.getControllerInfo().getOutputPackage(), moduleName);
        //子包名已经包含了完整路径
        builder.parent("")
                .moduleName("")
                .entity(entityPkg)
                .controller(controllerPkg)
                .mapper(mapperPkg)
                .service(servicePkg)
                .serviceImpl(serviceImplPkg)
                .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlOutputPath));
    }

    private void configTemplate(TemplateConfig.Builder builder, List<String> choosedFileTypes, UserConfig userConfig) {
        builder.entity(findTemplatePath(Constant.FILE_TYPE_ENTITY, userConfig));
        builder.mapper(findTemplatePath(Constant.FILE_TYPE_MAPPER, userConfig));
        builder.xml(findTemplatePath(Constant.FILE_TYPE_MAPPER_XML, userConfig));
        builder.service(findTemplatePath(Constant.FILE_TYPE_SERVICE, userConfig));
        builder.serviceImpl(findTemplatePath(Constant.FILE_TYPE_SERVICEIMPL, userConfig));
        builder.controller(findTemplatePath(Constant.FILE_TYPE_CONTROLLER, userConfig));
        if (!choosedFileTypes.contains(Constant.FILE_TYPE_ENTITY)) {
            builder.disable(TemplateType.ENTITY);
        }
        if (!choosedFileTypes.contains(Constant.FILE_TYPE_MAPPER)) {
            builder.disable(TemplateType.MAPPER);
        }
        if (!choosedFileTypes.contains(Constant.FILE_TYPE_MAPPER_XML)) {
            builder.disable(TemplateType.XML);
        }
        if (!choosedFileTypes.contains(Constant.FILE_TYPE_SERVICE)) {
            builder.disable(TemplateType.SERVICE);
        }
        if (!choosedFileTypes.contains(Constant.FILE_TYPE_SERVICEIMPL)) {
            builder.disable(TemplateType.SERVICE_IMPL);
        }
        if (!choosedFileTypes.contains(Constant.FILE_TYPE_CONTROLLER)) {
            builder.disable(TemplateType.CONTROLLER);
        }
    }

    //自定义模板参数配置
    private void configInjection(InjectionConfig.Builder builder, UserConfig userConfig, GenSetting genSetting) {
        //自定义参数
        builder.beforeOutputFile((tableInfo, objectMap) -> {
            TemplateVaribleInjecter varibleInjecter = generatorConfig.getTemplateVaribleInjecter();
            Map<String, Object> vars = null;
            if (varibleInjecter != null) {
                vars = varibleInjecter.getCustomTemplateVaribles(tableInfo);
            }
            if (vars == null) {
                vars = Maps.newHashMap();
            }
            //用于控制controller中对应API是否展示的自定义参数
            Map<String, Object> controllerMethodsVar = Maps.newHashMap();
            for (String method : genSetting.getChoosedControllerMethods()) {
                controllerMethodsVar.put(method, true);
            }
            if (controllerMethodsVar.size() > 0) {
                controllerMethodsVar.put("hasMethod", true);
            }
            vars.put("controllerMethods", controllerMethodsVar);
            if (!StrUtil.isEmpty(generatorConfig.getSchemaName())) {
                vars.put("schemaName", generatorConfig.getSchemaName() + ".");
            }
            objectMap.putAll(vars);
        });
        //自定义文件生成
        for (OutputFileInfo outputFileInfo : userConfig.getOutputFiles()) {
            if (!outputFileInfo.isBuiltIn()
                    && genSetting.getChoosedOutputFiles().contains(outputFileInfo.getFileType())) {
                CustomFile.Builder fileBuilder = new CustomFile.Builder();
                //注意这里传入的是fileType,配合自定义的TemplateEngine.outputCustomFile生成自定义文件
                fileBuilder.fileName(outputFileInfo.getFileType());
                fileBuilder.templatePath(outputFileInfo.getTemplatePath());
                fileBuilder.packageName(outputFileInfo.getOutputPackage());
                if (genSetting.isOverride()) {
                    fileBuilder.enableFileOverride();
                }
                builder.customFile(fileBuilder.build());
            }
        }
    }


    private void checkGenSetting(GenSetting genSetting) {
        if (StrUtil.isEmpty(genSetting.getRootPath())) {
            throw new ServiceException("目标项目根目录不能为空");
        }
        genSetting.setRootPath(projectPathResolver.getUTF8String(genSetting.getRootPath()));
        if (!FileUtil.isDirectory(genSetting.getRootPath())) {
            throw new ServiceException("目标项目根目录错误，请确认目录有效且存在：" + genSetting.getRootPath());
        }
        if (!genSetting.getRootPath().endsWith(File.separator)) {
            genSetting.setRootPath(genSetting.getRootPath() + File.separator);
        }
    }

    /**
     * 配置entity的生成信息
     */
    private void configEntity(Entity.Builder entityBuilder, EntityStrategy entityStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfig.getAvailableNameConverter();
        entityBuilder.idType(generatorConfig.getIdType());
        entityBuilder.nameConvert(new INameConvert() {
            @Override
            public String entityNameConvert(TableInfo tableInfo) {
                return nameConverter.entityNameConvert(tableInfo.getName(), generatorConfig.getTablePrefix());
            }

            @Override
            public String propertyNameConvert(TableField field) {
                return nameConverter.propertyNameConvert(field.getName());
            }
        });
        entityBuilder.superClass(entityStrategy.getSuperEntityClass());
        if (fileOverride) {
            entityBuilder.enableFileOverride();
        }
        if (!entityStrategy.isEntitySerialVersionUID()) {
            entityBuilder.disableSerialVersionUID();
        }
        if (entityStrategy.isEntityBuilderModel()) {
            entityBuilder.enableChainModel();
        }
        if (entityStrategy.isEntityLombokModel()) {
            entityBuilder.enableLombok();
        }
        if (entityStrategy.isEntityBooleanColumnRemoveIsPrefix()) {
            entityBuilder.enableRemoveIsPrefix();
        }
        if (entityStrategy.isEntityTableFieldAnnotationEnable()) {
            entityBuilder.enableTableFieldAnnotation();
        }
        if (entityStrategy.isActiveRecord()) {
            entityBuilder.enableActiveRecord();
        }
        if (!StrUtil.isEmpty(entityStrategy.getVersionFieldName())) {
            entityBuilder.versionColumnName(entityStrategy.getVersionFieldName());
            entityBuilder.versionPropertyName(entityStrategy.getVersionFieldName());
        }
        if (!StrUtil.isEmpty(entityStrategy.getLogicDeleteFieldName())) {
            entityBuilder.logicDeleteColumnName(entityStrategy.getLogicDeleteFieldName());
            entityBuilder.logicDeletePropertyName(entityStrategy.getLogicDeleteFieldName());
        }
        if (entityStrategy.getSuperEntityColumns() != null) {
            entityBuilder.addSuperEntityColumns(entityStrategy.getSuperEntityColumns());
        }
        if (entityStrategy.getTableFills() != null
                && !entityStrategy.getTableFills().isEmpty()) {
            List<IFill> tableFills = new ArrayList<>();
            for (String tableFillStr : entityStrategy.getTableFills()) {
                if (!StrUtil.isEmpty(tableFillStr)) {
                    String[] tmp = tableFillStr.split(":");
                    IFill tableFill = new Column(tmp[0], FieldFill.valueOf(tmp[1].toUpperCase()));
                    tableFills.add(tableFill);
                }
            }
            entityBuilder.addTableFills(tableFills);
        }
        entityBuilder.idType(generatorConfig.getIdType());
        entityBuilder.convertFileName(entityName -> entityName);
    }

    /**
     * 配置mapper和mapper-xml
     */
    private void configMapper(Mapper.Builder mapperBuilder, MapperStrategy mapperStrategy, MapperXmlStrategy mapperXmlStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfig.getAvailableNameConverter();
        if (mapperStrategy.getSuperMapperClass() != null) {
            mapperBuilder.superClass(mapperStrategy.getSuperMapperClass());
        }
        if (mapperXmlStrategy.isBaseResultMap()) {
            mapperBuilder.enableBaseResultMap();
            //TODO:enableBaseColumnList，cache目前没有页面配置
            mapperBuilder.enableBaseColumnList();
        }
        mapperBuilder.convertMapperFileName(nameConverter::mapperNameConvert);
        mapperBuilder.convertXmlFileName(nameConverter::mapperXmlNameConvert);
        if (fileOverride) {
            mapperBuilder.enableFileOverride();
        }
    }

    /**
     * 配置service
     */
    private void configService(Service.Builder serviceBuilder, ServiceStrategy serviceStrategy, ServiceImplStrategy serviceImplStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfig.getAvailableNameConverter();
        if (fileOverride) {
            serviceBuilder.enableFileOverride();
        }
        if (serviceStrategy.getSuperServiceClass() != null) {
            serviceBuilder.superServiceClass(serviceStrategy.getSuperServiceClass());
        }
        if (serviceImplStrategy.getSuperServiceImplClass() != null) {
            serviceBuilder.superServiceImplClass(serviceImplStrategy.getSuperServiceImplClass());
        }
        serviceBuilder.convertServiceFileName(nameConverter::serviceNameConvert);
        serviceBuilder.convertServiceImplFileName(nameConverter::serviceImplNameConvert);
    }

    /**
     * 配置Controller
     */
    private void configController(Controller.Builder controllerBuilder, ControllerStrategy controllerStrategy, boolean fileOverride) {
        NameConverter nameConverter = generatorConfig.getAvailableNameConverter();
        if (fileOverride) {
            controllerBuilder.enableFileOverride();
        }
        if (controllerStrategy.isRestControllerStyle()) {
            controllerBuilder.enableRestStyle();
        }
        if (controllerStrategy.isControllerMappingHyphenStyle()) {
            controllerBuilder.enableHyphenStyle();
        }
        if (controllerStrategy.getSuperControllerClass() != null) {
            controllerBuilder.superClass(controllerStrategy.getSuperControllerClass());
        }
        controllerBuilder.convertFileName(nameConverter::controllerNameConvert);
    }


    private OutputFileInfo findFileConfigByType(String fileType, UserConfig userConfig) {
        for (OutputFileInfo outputFileInfo : userConfig.getOutputFiles()) {
            if (fileType.equals(outputFileInfo.getFileType())) {
                return outputFileInfo;
            }
        }
        return null;
    }

    private String getOutputPathByFileType(String fileType, UserConfig userConfig) {
        return projectPathResolver.convertPackageToPath(Objects.requireNonNull(findFileConfigByType(fileType, userConfig)).getOutputLocation());
    }

    private String findTemplatePath(String fileType, UserConfig userConfig) {
        return Objects.requireNonNull(findFileConfigByType(fileType, userConfig)).getAvailableTemplatePath();
    }

}
