package com.github.davidfantasy.mybatisplus.generatorui;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.TemplateVaribleInjecter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.*;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.util.HashMap;
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

    public static void main(String[] args) {


        String jdbUrl = "jdbc:mysql://223.223.223.175:3307/ops_platform?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&allowMultiQueries=true";
        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl(jdbUrl)
                .userName("dbadmin")
                .password("dz@db123")
                .driverClassName("com.mysql.jdbc.Driver")
                .basePackage("com.hzzh.ops")
                .port(8069)
                .build();
//        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl("jdbc:mysql://192.168.1.211:3306/cimc-user-center")
//                .userName("root")
//                .password("root")
//                .port(8068)
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .basePackage("com.github.davidfantasy.mybatisplus.generatorui.example")
//                .build();
        run(config);
    }

    public static void run(GeneratorConfig generatorConfig) {
        if (Strings.isNullOrEmpty(generatorConfig.getJdbcUrl())) {
            throw new IllegalArgumentException("jdbcUrl必须要设置");
        }
        MybatisPlusToolsApplication.generatorConfig = generatorConfig;
        Map<String, Object> props = Maps.newHashMap();
        props.put("spring.mvc.static-path-pattern", "/static/**");
        props.put("spring.resources.static-locations", "classpath:/static");
        new SpringApplicationBuilder()
                .properties(props)
                .sources(MybatisPlusToolsApplication.class)
                .run(new String[0]);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerConfig(GeneratorConfig config) {
        return factory -> {
            if (config.getPort() != null) {
                factory.setPort(MybatisPlusToolsApplication.generatorConfig.getPort());
            } else {
                factory.setPort(8080);
            }
        };
    }

    @Bean
    public GeneratorConfig generatorConfig() {
        return MybatisPlusToolsApplication.generatorConfig;
    }


}

