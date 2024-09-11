package com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultGenerator;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;

/**
 * The type Multiple open api actuator resource.
 *
 * @author bnasslashen
 */
@RestControllerEndpoint(id = DEFAULT_API_DOCS_ACTUATOR_URL)
public class MultipleOpenApiActuatorResource {

    @GetMapping(value = "/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result openapiJson(@PathVariable String group)
            throws JsonProcessingException {
        return ResultGenerator.genSuccessResult("你好，" + group);
    }

}
