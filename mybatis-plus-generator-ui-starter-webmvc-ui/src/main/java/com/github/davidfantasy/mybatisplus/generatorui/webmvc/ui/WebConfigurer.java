package com.github.davidfantasy.mybatisplus.generatorui.webmvc.ui;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration.MybatisplusGeneratoruiAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants.SPRINGDOC_USE_ROOT_PATH;

/**
 * Spring MVC 配置
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(MybatisplusGeneratoruiAutoConfiguration.class)
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/").setViewName("forward:/index.html");
        // registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new MyErrorPageRegistrar();
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

    private static class MyErrorPageRegistrar implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
        }
    }
}
