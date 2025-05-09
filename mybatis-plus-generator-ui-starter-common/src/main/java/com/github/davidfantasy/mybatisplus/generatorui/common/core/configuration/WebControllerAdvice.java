package com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration;

import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultCode;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

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