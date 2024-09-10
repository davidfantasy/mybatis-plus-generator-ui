package com.github.davidfantasy.mybatisplus.generatorui;

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
import org.springframework.context.annotation.Bean;
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

    private static GeneratorConfig generatorConfig;

    public static void run(GeneratorConfig generatorConfig) {
        if (Strings.isNullOrEmpty(generatorConfig.getJdbcUrl())) {
            throw new IllegalArgumentException("jdbcUrl必须要设置");
        }
        //本项目的综合配置
        MybatisPlusToolsApplication.generatorConfig = generatorConfig;

        //启动MyBatis
        Map<String, Object> props = Maps.newHashMap();
        new SpringApplicationBuilder()
                .properties(props)
                .sources(MybatisPlusToolsApplication.class)
                .run();
    }

    /**
     * 通过注入一个WebServerFactoryCustomizer来达到修改服务器端口的目的
     * @param config
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerConfig(GeneratorConfig config) {
        return factory -> {
            if (config.getPort() != null) {
                factory.setPort(MybatisPlusToolsApplication.generatorConfig.getPort());
            } else {
                factory.setPort(8080);
            }
            factory.setContextPath("");
        };
    }

    /**
     * 注入项目配置
     * @return 项目配置的对象
     */
    @Bean
    public GeneratorConfig generatorConfig() {
        return MybatisPlusToolsApplication.generatorConfig;
    }


}

