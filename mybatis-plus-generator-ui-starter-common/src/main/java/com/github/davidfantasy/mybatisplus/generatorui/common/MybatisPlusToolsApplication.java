package com.github.davidfantasy.mybatisplus.generatorui.common;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.properties.GeneratorConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.*;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
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

    public static void run(Class<?> clazz, String[] args, GeneratorConfig generatorConfig) {
        if (Strings.isNullOrEmpty(generatorConfig.getJdbcUrl())) {
            throw new IllegalArgumentException("jdbcUrl必须要设置");
        }

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
}

