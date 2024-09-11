package com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.core;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration.MybatisplusGeneratoruiAutoConfiguration;
import com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.api.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import static com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants.MYBATISPLUS_GENETATORUI_ENABLED;

/**
 * The type Spring doc web mvc configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = MYBATISPLUS_GENETATORUI_ENABLED, matchIfMissing = true)
@ConditionalOnBean(MybatisplusGeneratoruiAutoConfiguration.class)
@Import({AutoCompleteController.class,
        DatabaseController.class,
        MbpGeneratorController.class,
        OpenApiWebMvcResource.class,
        OutputFileInfoController.class,
        SqlGeneratorController.class,
        TemplateController.class})
public class SpringDocWebMvcConfiguration {
}
