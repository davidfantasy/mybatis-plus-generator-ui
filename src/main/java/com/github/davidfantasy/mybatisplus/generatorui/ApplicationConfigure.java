package com.github.davidfantasy.mybatisplus.generatorui;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.BeetlTemplateEngine;
import com.github.davidfantasy.mybatisplus.generatorui.service.UserConfigStore;
import com.github.davidfantasy.mybatisplus.generatorui.sqlparser.DynamicParamSqlEnhancer;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ApplicationConfigure {

    @Bean
    public ProjectPathResolver projectPathResolver(GeneratorConfig config) {
        return new ProjectPathResolver(config.getBasePackage());
    }


    /**
     * 这里的DataSourceConfig仅用于获取tablesql，查询数据库元数据，和代码生成无关
     * 代码生成的数据库配置参考MbpGenerator.initGenerator
     */
    @Bean
    public DataSourceConfig mbpDsConfig(GeneratorConfig config) {
        return new DataSourceConfig.Builder(config.getJdbcUrl(), config.getUserName(), config.getPassword())
                .schema(config.getSchemaName())
                .typeConvert(config.getTypeConvert())
                .build();
    }

    /**
     * 用于查询数据库元数据
     */
    @Bean
    public JdbcTemplate jdbcTemplate(GeneratorConfig config) {
        if (StrUtil.isBlank(config.getJdbcUrl())) {
            throw new IllegalArgumentException("必须指定jdbcUrl用于创建数据源");
        }
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(config.getJdbcUrl());
        ds.setUsername(config.getUserName());
        ds.setPassword(config.getPassword());
        ds.setDriverClassName(config.getDriverClassName());
        return new JdbcTemplate(ds);
    }

    @Bean
    public BeetlTemplateEngine beetlTemplateEngine(GeneratorConfig config, UserConfigStore userConfigStore) {
        return new BeetlTemplateEngine(config.getNameConverter(), userConfigStore.getTemplateStoreDir());
    }

    @Bean
    public DynamicParamSqlEnhancer dynamicParamSqlEnhancer(DataSourceConfig dsc) {
        return new DynamicParamSqlEnhancer(dsc.getDbType());
    }
}
