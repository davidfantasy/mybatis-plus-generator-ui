package com.github.davidfantasy.mybatisplus.generatorui.webmvc.api.api;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.properties.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.api.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.common.dto.GenDtoFromSqlReq;
import com.github.davidfantasy.mybatisplus.generatorui.common.service.SqlGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sql")
public class SqlGeneratorController {

    @Autowired
    private SqlGeneratorService sqlGeneratorService;

    @Autowired
    private GeneratorConfig generatorConfig;

    @GetMapping("/basepackage")
    public Result getBasepackage() {
        return ResultGenerator.genSuccessResult(generatorConfig.getBasePackage());
    }


    @PostMapping("/gen-mapper-method")
    public Result genMapperMethodFromSQL(@RequestBody GenDtoFromSqlReq params) throws Exception {
        sqlGeneratorService.genMapperMethod(params);
        return ResultGenerator.genSuccessResult();
    }


}
