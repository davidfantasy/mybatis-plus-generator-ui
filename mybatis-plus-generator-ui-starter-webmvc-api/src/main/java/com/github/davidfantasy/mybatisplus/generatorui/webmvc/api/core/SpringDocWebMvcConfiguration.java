package com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.core;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration.SpringDocConfiguration;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.providers.SpringWebProvider;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.function.RouterFunction;

import static com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants.SPRINGDOC_ENABLED;

/**
 * The type Spring doc web mvc configuration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocWebMvcConfiguration {

    static {
    }

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
     * The type Spring doc web mvc router configuration.
     *
     * @author bnasslahsen
     */
    @ConditionalOnClass(RouterFunction.class)
    static class SpringDocWebMvcRouterConfiguration {

        /**
         * Router function provider router function provider.
         *
         * @return the router function provider
         */
        @Bean
        @ConditionalOnMissingBean
        @Lazy(false)
        RouterFunctionWebMvcProvider routerFunctionProvider() {
            return new RouterFunctionWebMvcProvider();
        }
    }
}
