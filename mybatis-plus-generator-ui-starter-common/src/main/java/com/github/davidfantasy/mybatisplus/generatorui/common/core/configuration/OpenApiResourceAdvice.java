package com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration;

import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
