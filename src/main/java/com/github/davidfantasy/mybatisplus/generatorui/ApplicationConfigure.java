package com.github.davidfantasy.mybatisplus.generatorui;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfigure {

    @Bean
    public ProjectPathResolver projectPathResolver(GeneratorConfig config) {
        return new ProjectPathResolver(config.getBasePackage());
    }

    @Bean
    public DataSource dataSource(GeneratorConfig config) {
        if (StrUtil.isBlank(config.getJdbcUrl())) {
            throw new IllegalArgumentException("必须指定jdbcUrl用于创建数据源");
        }
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(config.getJdbcUrl());
        ds.setUsername(config.getUserName());
        ds.setPassword(config.getPassword());
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    @Bean
    public DataSourceConfig dataSourceConfig(GeneratorConfig config) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(config.getJdbcUrl());
        dataSourceConfig.setDriverName(config.getDriverClassName());
        dataSourceConfig.setUsername(config.getUserName());
        dataSourceConfig.setPassword(config.getPassword());
        return dataSourceConfig;
    }

    @Bean
    public JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
