package com.github.davidfantasy.mybatisplus.generatorui.controller;

import com.github.davidfantasy.mybatisplus.generatorui.common.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.dto.MpgGenCodeDto;
import com.github.davidfantasy.mybatisplus.generatorui.service.MbpGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mbp-generator")
public class MbpGeneratorController {

    @Autowired
    private MbpGeneratorService mbpGeneratorService;

    @PostMapping("/gen-code")
    public Result genCode(@RequestBody MpgGenCodeDto dto) {
        mbpGeneratorService.genCodeBatch(dto.getGenSetting(), dto.getTables());
        return ResultGenerator.genSuccessResult();
    }

}
