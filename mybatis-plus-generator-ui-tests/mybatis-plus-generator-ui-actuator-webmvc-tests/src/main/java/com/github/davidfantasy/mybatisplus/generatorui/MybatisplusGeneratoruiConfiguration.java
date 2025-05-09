package com.github.davidfantasy.mybatisplus.generatorui;

import com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants;
import com.github.davidfantasy.mybatisplus.generatorui.common.mbp.NameConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = Constants.MYBATISPLUS_GENETATORUI_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication
public class MybatisplusGeneratoruiConfiguration {
    /**
     * 注入一个方便替换名字的类
     * @return
     */
    @Bean
    public NameConverter nameConverter() {
        return new NameConverter() {
            @Override
            public String serviceNameConvert(String entityName) {
                return entityName + "Service1";
            }

            @Override
            public String controllerNameConvert(String entityName) {
                return entityName + "Action";
            }
        };
    }
}
