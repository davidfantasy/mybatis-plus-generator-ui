package com.github.davidfantasy.mybatisplus.generatorui.webmvc.ui;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration.SpringDocConfiguration;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.providers.SpringWebProvider;
import com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.core.SpringWebMvcProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants.SPRINGDOC_SWAGGER_UI_ENABLED;
import static com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants.SPRINGDOC_USE_ROOT_PATH;


/**
 * The type Swagger config.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = SPRINGDOC_SWAGGER_UI_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SwaggerConfig {

    /**
     * Spring web provider spring web provider.
     *
     * @return the spring web provider
     */
    @Bean
    @ConditionalOnMissingBean
    @Lazy(false)
    SpringWebProvider springWebProvider() {
        return new SpringWebMvcProvider();
    }

    /**
     * Swagger ui home swagger ui home.
     *
     * @return the swagger ui home
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = SPRINGDOC_USE_ROOT_PATH, havingValue = "true")
    @Lazy(false)
    UiHome swaggerUiHome() {
        return new UiHome();
    }
}
