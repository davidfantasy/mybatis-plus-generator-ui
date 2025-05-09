package com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.properties.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.mbp.BeetlTemplateEngine;
import com.github.davidfantasy.mybatisplus.generatorui.common.service.UserConfigStore;
import com.github.davidfantasy.mybatisplus.generatorui.common.sqlparser.DynamicParamSqlEnhancer;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfigure {
    /**
     * 通过注入一个WebServerFactoryCustomizer来达到修改服务器端口的目的
     *
     * @param config
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerConfig(GeneratorConfig config) {
        return factory -> {
            if (config.getPort() != null) {
                factory.setPort(config.getPort());
            } else {
                factory.setPort(8080);
            }
            factory.setContextPath("");
        };
    }

    @Bean
    public ProjectPathResolver projectPathResolver(GeneratorConfig config) {
        return new ProjectPathResolver(config.getBasePackage());
    }

    /**
     * 如果没有，就主动注入一个
     *
     * @param config
     * @return
     */
    @ConditionalOnMissingBean(DataSource.class)
    @Bean
    public DataSource dataSource(GeneratorConfig config) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(config.getJdbcUrl());
        ds.setUsername(config.getUserName());
        ds.setPassword(config.getPassword());
        ds.setDriverClassName(config.getDriverClassName());
        return ds;
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
    public JdbcTemplate jdbcTemplate(GeneratorConfig config, ObjectProvider<DataSource> dataSources) {
        if (StrUtil.isBlank(config.getJdbcUrl())) {
            throw new IllegalArgumentException("必须指定jdbcUrl用于创建数据源");
        }
        DataSource ds = dataSources.getIfAvailable();
        if (ds == null) {
            ds = dataSource(config);
        }
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
