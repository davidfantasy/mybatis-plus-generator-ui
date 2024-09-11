package com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration;

import com.github.davidfantasy.mybatisplus.generatorui.common.ApplicationConfigure;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = Constants.SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication
@Import({ApplicationConfigure.class})
public class SpringDocConfiguration {

    /**
     * The constant BINDRESULT_CLASS.
     */
    private static final String BINDRESULT_CLASS = "org.springframework.boot.context.properties.bind.BindResult";

    /**
     * Local spring doc parameter name discoverer local variable table parameter name discoverer.
     *
     * @return the local variable table parameter name discoverer
     */
    @Bean
    @Lazy(false)
    LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
        return new LocalVariableTableParameterNameDiscoverer();
    }

    /**
     * The type Open api resource advice.
     *
     * @author bnasslashen
     */
    @RestControllerAdvice
    class OpenApiResourceAdvice {
        /**
         * Handle no handler found response entity.
         *
         * @param e the e
         * @return the response entity
         */
        @ExceptionHandler(ServiceException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<Result> handleNoHandlerFound(ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResultGenerator.genFailResult(e.getMessage()));
        }
    }
}
