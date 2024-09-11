package com.github.davidfantasy.mybatisplus.generatorui.common;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.*;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;


/**
 * 不使用@SpringBootApplication和@EnableAutoConfiguration
 * 注解，避免启动时被宿主系统的自动配置所干扰，直接注入需要的配置类
 */
@SpringBootConfiguration
@Import({
        DispatcherServletAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        WebMvcAutoConfiguration.class})
@ComponentScan("com.github.davidfantasy.mybatisplus.generatorui")
@Slf4j
public class MybatisPlusToolsApplication {

    private static GeneratorConfig generatorConfig;

    public static void run(Class<?> clazz, String[] args, GeneratorConfig generatorConfig) {
        if (Strings.isNullOrEmpty(generatorConfig.getJdbcUrl())) {
            throw new IllegalArgumentException("jdbcUrl必须要设置");
        }
        //本项目的综合配置
        MybatisPlusToolsApplication.generatorConfig = generatorConfig;

        //启动MyBatis
        Map<String, Object> props = Maps.newHashMap();
        props.put("spring.datasource.url", generatorConfig.getJdbcUrl());
        props.put("spring.datasource.driver-class-name", generatorConfig.getDriverClassName());
        props.put("spring.datasource.username", generatorConfig.getUserName());
        props.put("spring.datasource.password", generatorConfig.getPassword());
        ConfigurableApplicationContext run = new SpringApplicationBuilder()
                .properties(props)
                .sources(clazz)
                .run(args);

    }

/*    @Configuration
    public static class MainConfigutration {


        *//**
         * 注入项目配置
         *
         * @return 项目配置的对象
         *//*
        @Bean
        public GeneratorConfig generatorConfig() {
            return MybatisPlusToolsApplication.generatorConfig;
        }

    }*/
}

