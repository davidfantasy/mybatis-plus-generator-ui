package com.github.davidfantasy.mybatisplus.generatorui;

import com.google.common.collect.Lists;
import jdk.internal.joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;


@SpringBootApplication
@Slf4j
public class MybatisPlusToolsApplication {

    private static GeneratorConfig generatorConfig;

    public static void main(String[] args) {
        run(null);
    }

    public static void run(GeneratorConfig generatorConfig) {
        List<String> properties = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(generatorConfig.getJdbcUrl())) {
            properties.add("jdbcUrl=" + generatorConfig.getJdbcUrl());
            properties.add("userName=" + generatorConfig.getUserName());
            properties.add("password=" + generatorConfig.getPassword());
        } else {
            throw new IllegalArgumentException("必须指定jdbcUrl用于创建数据源");
        }
        new SpringApplicationBuilder().sources(MybatisPlusToolsApplication.class)
                .properties(properties.toArray(new String[properties.size()]))
                .run(new String[0]);
    }

    @Bean
    public ProjectPathResolver projectPathResolver() {
        return new ProjectPathResolver();
    }

}

