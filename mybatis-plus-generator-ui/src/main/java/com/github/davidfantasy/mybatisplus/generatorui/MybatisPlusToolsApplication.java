package com.github.davidfantasy.mybatisplus.generatorui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;


@SpringBootApplication
@Slf4j
public class MybatisPlusToolsApplication {

    public static void main(String[] args) {
        run(MybatisPlusToolsApplication.class, null);
    }

    public static void run(Class<?> primarySource, GeneratorConfig generatorConfig)  {
        log.info("primarySource code source location:{}", primarySource.getProtectionDomain().getCodeSource().getLocation().getFile());
        log.info("jar code source location:{}", MybatisPlusToolsApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        log.info("usr dir:{}", System.getProperty("user.dir"));
        try {
            log.info("thread dir:{}", Thread.currentThread().getContextClassLoader().getResource("application.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(MybatisPlusToolsApplication.class, new String[0]);
    }

}

