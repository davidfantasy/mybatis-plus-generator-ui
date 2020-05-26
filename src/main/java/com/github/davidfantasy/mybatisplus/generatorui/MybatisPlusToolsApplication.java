package com.github.davidfantasy.mybatisplus.generatorui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

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
        if (Strings.isNullOrEmpty(generatorConfig.getJdbcUrl())) {
            throw new IllegalArgumentException("jdbcUrl必须要设置");
        }
        MybatisPlusToolsApplication.generatorConfig = generatorConfig;
        new SpringApplicationBuilder().sources(MybatisPlusToolsApplication.class)
                .properties(properties.toArray(new String[properties.size()]))
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

