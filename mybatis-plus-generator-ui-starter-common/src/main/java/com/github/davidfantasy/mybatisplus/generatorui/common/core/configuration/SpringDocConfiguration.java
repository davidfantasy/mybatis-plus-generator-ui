package com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration;

import com.github.davidfantasy.mybatisplus.generatorui.common.ApplicationConfigure;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultCode;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    /**
     * 用于全局异常处理
     */
    @ControllerAdvice
    @Slf4j
    public class WebControllerAdvice {

        @ExceptionHandler(value = Exception.class)
        @ResponseBody
        public Result exceptionHandler(Exception e) {
            Result result = new Result();
            log.info("未捕获的异常：" + e.getMessage(), e);
            if (e instanceof ServiceException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
                NoHandlerFoundException ex = (NoHandlerFoundException) e;
                result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + ex.getRequestURL() + "] 不存在");
            } else if (e instanceof RuntimeException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("系统发生内部错误，请联系管理员");
                log.error("系统发生内部错误，请查看控制台日志了解详情", e);
            }
            return result;
        }

    }
}
