package com.github.davidfantasy.mybatisplus.generatorui.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.*;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.BeetlTemplateEngine;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.DOT_JAVA;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.DOT_XML;

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


    public void genDtoFileFromSQL(GenDtoFromSqlReq params) throws Exception {
        if (Strings.isNullOrEmpty(params.getSql())) {
            throw new ServiceException("数据源SQL不能为空");
        }
        String decodedSql = new String(decoder.decode(params.getSql()), "UTF-8");
        if (!decodedSql.trim().toLowerCase().startsWith("select")) {
            throw new ServiceException("只能通过查询语句生成DTO对象，请检查SQL");
        }
        SqlRowSet rowSet = null;
        try {
            rowSet = jdbcTemplate.queryForRowSet(decodedSql);
        } catch (Exception e) {
            log.error("执行SQL发生错误", e);
            throw new ServiceException("执行SQL发生错误：" + e.getMessage());
        }
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<SelectResultField> fields = Lists.newArrayList();

        GlobalConfig mbpConfig = new GlobalConfig();
        mbpConfig.setDateType(generatorConfig.getDateType());

        for (int i = 1; i <= columnCount; i++) {
            SelectResultField resultField = new SelectResultField();
            resultField.setColumnName(metaData.getColumnLabel(i));
            //将数据库类型转换为java类型
            String colType = metaData.getColumnTypeName(i);
            IColumnType columnType = dataSourceConfig.getTypeConvert().processTypeConvert(mbpConfig, metaData.getColumnTypeName(i));
            resultField.setJavaType(columnType.getType());
            params.getConfig().addImportPackage(columnType.getPkg());
            resultField.setPropertyName(generatorConfig.getAvailableNameConverter().propertyNameConvert(resultField.getColumnName()));
            fields.add(resultField);
        }
        params.getConfig().setFields(fields);
        params.getConfig().setCreateDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (Strings.isNullOrEmpty(params.getConfig().getMapperLocation())) {
            params.getConfig().setComment(params.getConfig().getMapperLocation() + "的查询结果集，该代码由mybatis-plus-generator-ui自动生成");
        }else{
            params.getConfig().setComment("该代码由mybatis-plus-generator-ui自动生成");
        }
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", params.getConfig());
        String outputPath = projectPathResolver.convertPackageToPath(params.getConfig().getFullPackage()) + DOT_JAVA;
        File file = new File(outputPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        beetlTemplateEngine.writer(tplParams, "classpath:templates/dto.btl", outputPath);
        log.info("DTO已成功生成，输出位置为:" + outputPath);
        //生成mapper中的方法和ResultMap
        if (!Strings.isNullOrEmpty(params.getConfig().getMapperLocation())) {
            genMapperElementsFromSql(decodedSql, params.getConfig());
        }
    }

    public void genMapperElementsFromSql(String sql, GenDtoConfig config) throws IOException, DocumentException {
        Map<String, Object> tplParams = Maps.newHashMap();
        tplParams.put("config", config);
        tplParams.put("elementType", "select");
        tplParams.put("sql", sql);
        String resultMapStr = beetlTemplateEngine.write2String(tplParams, "classpath:templates/resultMap.btl");
        String queryEleStr = beetlTemplateEngine.write2String(tplParams, "classpath:templates/mapperMethods.btl");
        MapperElement resultMapEle = MapperElement.builder().id(config.getDtoName() + "Map")
                .comment(config.getMapperElementId() + "的结果映射配置，由mybatis-plus-generator-ui自动生成")
                .content(resultMapStr)
                .location(ElementPosition.FIRST).build();
        MapperElement queryEle = MapperElement.builder().id(config.getMapperElementId())
                .comment("由mybatis-plus-generator-ui自动生成")
                .content(queryEleStr)
                .location(ElementPosition.LAST).build();
        String mapperPath = projectPathResolver.convertPackageToPath(config.getMapperPackage()) + DOT_XML;
        mapperXmlParser.addElementInMapper(mapperPath, resultMapEle, queryEle);
        log.info("ResultMap和Mapper方法已生成，输出位置为:" + mapperPath);
    }

}
