package com.github.davidfantasy.mybatisplus.generatorui.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.*;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.BeetlTemplateEngine;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.ConditionExpr;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.DynamicParamSqlEnhancer;
import com.github.davidfantasy.mybatisplus.generatorui.util.PathUtil;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_JAVA;
import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_XML;


@Service
@Slf4j
public class SqlGeneratorService {

    final Base64.Decoder decoder = Base64.getDecoder();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GeneratorConfig generatorConfig;

    @Autowired
    private BeetlTemplateEngine beetlTemplateEngine;

    @Autowired
    private ProjectPathResolver projectPathResolver;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private MapperXmlParser mapperXmlParser;

    @Autowired
    private DynamicParamSqlEnhancer dynamicParamSqlEnhancer;

    @Autowired
    private JavaClassParser javaClassParser;

    private List<String> rangeOperators = Lists.newArrayList("BETWEEN", "<", "<=", ">", ">=");

    public void genMapperMethod(GenDtoFromSqlReq params) throws Exception {
        if (Strings.isNullOrEmpty(params.getSql())) {
            throw new ServiceException("数据源SQL不能为空");
        }
        String decodedSql = new String(decoder.decode(params.getSql()), "UTF-8");
        if (!decodedSql.trim().toLowerCase().startsWith("select")) {
            throw new ServiceException("只能通过查询语句生成DTO对象，请检查SQL");
        }
        if (!Strings.isNullOrEmpty(params.getConfig().getFullPackage())) {
            try {
                Class.forName(params.getConfig().getFullPackage());
            } catch (Throwable t) {
                params.getConfig().setAutoCreatedResultDto(true);
            }
        }
        if (params.getConfig().isAutoCreatedResultDto()) {
            genDtoFileFromSQL(decodedSql, params.getConfig());
        }
        String namespace = genMapperElementsFromSql(decodedSql, params.getConfig());
        //在Dao中插入与Mapper节点对应的方法
        if (params.getConfig().getEnableCreateDaoMethod()) {
            if ("bean".equals(params.getConfig().getDaoMethodParamType())) {
                genParamDtoFromSql(decodedSql, params.getConfig().getDaoMethodParamDto(), params.getConfig().isEnableLombok());
            }
            addDaoMethod(namespace, decodedSql, params.getConfig());
        }
    }

    public void genDtoFileFromSQL(String sql, GenDtoConfig config) throws Exception {
        sql = dynamicParamSqlEnhancer.clearIllegalStatements(sql);
        SqlRowSet rowSet = null;
        try {
            rowSet = jdbcTemplate.queryForRowSet(sql);
        } catch (Exception e) {
            log.error("执行SQL发生错误", e);
            throw new ServiceException("执行SQL发生错误：" + e.getMessage());
        }
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<DtoFieldInfo> fields = Lists.newArrayList();
        GlobalConfig.Builder gb = new GlobalConfig.Builder();
        GlobalConfig gc = gb.dateType(generatorConfig.getDateType()).build();
        for (int i = 1; i <= columnCount; i++) {
            DtoFieldInfo resultField = new DtoFieldInfo();
            resultField.setColumnName(metaData.getColumnLabel(i));
            //将数据库类型转换为java类型
            String colType = metaData.getColumnTypeName(i);
            IColumnType columnType = dataSourceConfig.getTypeConvert().processTypeConvert(gc, metaData.getColumnTypeName(i));
            resultField.setShortJavaType(columnType.getType());
            if (!Strings.isNullOrEmpty(columnType.getPkg())) {
                config.getImportPackages().add(columnType.getPkg());
            }
            resultField.setPropertyName(generatorConfig.getAvailableNameConverter().propertyNameConvert(resultField.getColumnName()));
            fields.add(resultField);
        }
        config.setFields(fields);
        config.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (!Strings.isNullOrEmpty(config.getMapperLocation())) {
            config.setComment(config.getMapperLocation() + "的查询结果集，该代码由mybatis-plus-generator-ui自动生成");
        } else {
            config.setComment("该代码由mybatis-plus-generator-ui自动生成");
        }
        genDtoFromConfig(config);
    }

    public String genMapperElementsFromSql(String sql, GenDtoConfig config) throws IOException, DocumentException {
        List<MapperElement> elements = Lists.newArrayList();
        //如果DTO是自动生成的，那么同时也生成结果映射集
        if (config.getResultMap() != null) {
            elements.add(createResultMapElement(config));
        }
        elements.add(createMapperMethodElement(sql, config));
        String mapperPath = projectPathResolver.convertPackageToPath(config.getMapperPackage()) + DOT_XML;
        String namespace = mapperXmlParser.addElementInMapper(mapperPath, elements.toArray(new MapperElement[]{}));
        log.info("ResultMap和Mapper方法已生成，输出位置为:" + mapperPath);
        return namespace;
    }

    public MapperElement createResultMapElement(GenDtoConfig config) {
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", config);
        String resultMapStr = beetlTemplateEngine.write2String(tplParams, "classpath:codetpls/resultMap.btl");
        MapperElement resultMapEle = MapperElement.builder().id(config.getDtoName() + "Map")
                .comment("Author:" + config.getAuthor() + "，Date:" + DateUtil.format(new Date(), "yyyy-MM-dd") + "，" + config.getMapperElementId() + "的结果映射配置，由mybatis-plus-generator-ui自动生成")
                .content(resultMapStr)
                .location(ElementPosition.FIRST).build();
        return resultMapEle;
    }

    public MapperElement createMapperMethodElement(String sql, GenDtoConfig config) {
        Map<String, Object> tplParams = Maps.newHashMap();
        String dbType = dataSourceConfig.getDbType().getDb();
        tplParams.put("config", config);
        tplParams.put("elementType", "select");
        if (config.isEnableParseDynamicParams()) {
            sql = dynamicParamSqlEnhancer.enhanceDynamicConditions(sql);
        }
        tplParams.put("sql", sql);
        String methodEleStr = beetlTemplateEngine.write2String(tplParams, "classpath:codetpls/mapperMethods.btl");
        MapperElement methodEle = MapperElement.builder().id(config.getMapperElementId())
                .comment("Author:" + config.getAuthor() + "，Date:" + DateUtil.format(new Date(), "yyyy-MM-dd") + ",由mybatis-plus-generator-ui自动生成")
                .content(methodEleStr)
                .location(ElementPosition.LAST).build();
        return methodEle;
    }

    public void genParamDtoFromSql(String sql, String paramDtoRef, boolean enableLombok) throws Exception {
        List<ConditionExpr> conditionExprs = dynamicParamSqlEnhancer.parseSqlDynamicConditions(sql);
        if (conditionExprs.isEmpty()) {
            log.info("未检测到SQL的动态参数，忽略参数DTO的生成");
            return;
        }
        List<DtoFieldInfo> fields = parseParamFieldsFromSql(sql);
        GenDtoConfig config = new GenDtoConfig();
        config.setEnableLombok(enableLombok);
        for (DtoFieldInfo fi : fields) {
            config.getImportPackages().addAll(fi.getImportJavaTypes());
        }
        config.setFullPackage(paramDtoRef);
        config.setFields(fields);
        config.setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (!Strings.isNullOrEmpty(config.getMapperLocation())) {
            config.setComment(config.getMapperLocation() + "的查询参数Bean，该代码由mybatis-plus-generator-ui自动生成");
        } else {
            config.setComment("该代码由mybatis-plus-generator-ui自动生成");
        }
        genDtoFromConfig(config);
    }

    /**
     * 根据相关配置生成DAO中的查询方法
     *
     * @param daoClassRef DAO的引用位置
     * @param sql         查询SQL语句
     * @param config      配置参数
     */
    public void addDaoMethod(String daoClassRef, String sql, GenDtoConfig config) throws Exception {
        Set<String> imports = Sets.newHashSet();
        List<DtoFieldInfo> methodParams = Lists.newArrayList();

        String returnType = "";
        if (!Strings.isNullOrEmpty(config.getFullPackage())) {
            imports.add("java.util.List");
            imports.add(config.getFullPackage());
            returnType = "List<" + PathUtil.getShortNameFromFullRef(config.getFullPackage()) + ">";
        } else {
            imports.add("java.util.List");
            imports.add("java.util.Map");
            returnType = "List<Map<String,Object>>";
        }
        //如果启用分页查询，则修改相关的参数和返回值
        if (config.isEnablePageQuery()) {
            imports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
            returnType = returnType.replaceFirst("List", "Page");
            DtoFieldInfo param = new DtoFieldInfo();
            param.setShortJavaType(PathUtil.getShortNameFromFullRef(returnType));
            param.setPropertyName("pageParam");
            methodParams.add(param);
        }
        List<DtoFieldInfo> sqlConditions = parseParamFieldsFromSql(sql);
        if (!sqlConditions.isEmpty()) {
            if ("map".equals(config.getDaoMethodParamType())) {
                DtoFieldInfo param = new DtoFieldInfo();
                param.setShortJavaType("Map<String,Object>");
                param.setPropertyName("params");
                param.addImportJavaType("java.util.Map");
                methodParams.add(param);
            } else if ("bean".equals(config.getDaoMethodParamType())) {
                DtoFieldInfo param = new DtoFieldInfo();
                param.setShortJavaType(PathUtil.getShortNameFromFullRef(config.getDaoMethodParamDto()));
                param.setPropertyName("params");
                param.addImportJavaType(config.getDaoMethodParamDto());
                methodParams.add(param);
            } else {
                for (DtoFieldInfo fieldInfo : sqlConditions) {
                    NodeList<AnnotationExpr> annotations = new NodeList();
                    AnnotationExpr paramAnno = new SingleMemberAnnotationExpr(new Name("Param"), new StringLiteralExpr(fieldInfo.getPropertyName()));
                    annotations.add(paramAnno);
                    fieldInfo.setAnnotations(annotations);
                    fieldInfo.addImportJavaType("org.apache.ibatis.annotations.Param");
                }
                methodParams = sqlConditions;
            }
        }
        for (DtoFieldInfo feild : methodParams) {
            if (feild.getImportJavaTypes() != null) {
                imports.addAll(feild.getImportJavaTypes());
            }
        }
        JavaClassMethodInfo methodInfo = JavaClassMethodInfo.builder()
                .classRef(daoClassRef)
                .methodName(config.getMapperMethod())
                .returnType(returnType)
                .importJavaTypes(imports)
                .params(methodParams).build();
        javaClassParser.addMethod2Interface(methodInfo);
    }


    private List<DtoFieldInfo> parseParamFieldsFromSql(String sql) {
        List<ConditionExpr> conditionExprs = dynamicParamSqlEnhancer.parseSqlDynamicConditions(sql);
        if (conditionExprs.isEmpty()) {
            return Lists.newArrayList();
        }
        List<DtoFieldInfo> fields = Lists.newArrayList();
        for (ConditionExpr expr : conditionExprs) {
            for (String paramName : expr.getParamNames()) {
                DtoFieldInfo field = new DtoFieldInfo();
                field.setPropertyName(PathUtil.getShortNameFromFullRef(paramName));
                boolean isDate = paramName.toLowerCase().endsWith("date")
                        || paramName.toLowerCase().endsWith("time");
                if (expr.getOperator().toUpperCase().equals("IN")) {
                    DbColumnType cType = DbColumnType.STRING;
                    if (isDate) {
                        cType = getRightDateType(generatorConfig.getDateType());
                        field.addImportJavaType(cType.getPkg());
                    }
                    field.setShortJavaType("List<" + cType.getType() + ">");
                    field.addImportJavaType("java.util.List");
                } else if (rangeOperators.contains(expr.getOperator().toUpperCase())) {
                    DbColumnType cType = DbColumnType.LONG;
                    if (isDate) {
                        cType = getRightDateType(generatorConfig.getDateType());
                        field.addImportJavaType(cType.getPkg());
                    }
                    field.setShortJavaType(cType.getType());
                } else {
                    field.setShortJavaType("String");
                }
                fields.add(field);
            }
        }
        return fields;
    }


    private void genDtoFromConfig(GenDtoConfig config) throws Exception {
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", config);
        String outputPath = projectPathResolver.convertPackageToPath(config.getFullPackage()) + DOT_JAVA;
        File file = new File(outputPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        beetlTemplateEngine.writer(tplParams, "classpath:codetpls/dto.btl", new File(outputPath));
        log.info("DTO已成功生成，输出位置为:" + outputPath);
    }


    private DbColumnType getRightDateType(DateType dateType) {
        switch (dateType) {
            case ONLY_DATE:
                return DbColumnType.DATE;
            case SQL_PACK:
                return DbColumnType.DATE_SQL;
            case TIME_PACK:
                return DbColumnType.LOCAL_DATE;
            default:
                return null;
        }
    }


}
