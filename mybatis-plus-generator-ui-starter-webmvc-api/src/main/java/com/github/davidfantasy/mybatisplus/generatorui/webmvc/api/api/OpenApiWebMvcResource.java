package com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.api;

import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants.API_DOCS_URL;

/**
 * The type Open api resource.
 *
 * @author bnasslahsen
 */
@RestController
public class OpenApiWebMvcResource {

    @GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result openapiJson(@Value(API_DOCS_URL) String apiDocsUrl) {
        return ResultGenerator.genSuccessResult("你好，" + apiDocsUrl);
    }

}