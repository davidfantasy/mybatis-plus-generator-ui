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


    @Bean
    public DataSourceConfig dataSourceConfig(GeneratorConfig config) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(config.getJdbcUrl());
        dataSourceConfig.setDriverName(config.getDriverClassName());
        dataSourceConfig.setUsername(config.getUserName());
        dataSourceConfig.setPassword(config.getPassword());
        dataSourceConfig.setSchemaName(config.getSchemaName());
        dataSourceConfig.setTypeConvert(config.getTypeConvert());
        return dataSourceConfig;
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
    public BeetlTemplateEngine beetlTemplateEngine(UserConfigStore userConfigStore) {
        return new BeetlTemplateEngine(userConfigStore.getTemplateStoreDir());
    }

    @Bean
    public DynamicParamSqlEnhancer dynamicParamSqlEnhancer(DataSourceConfig dsc) {
        return new DynamicParamSqlEnhancer(dsc);
    }
}
