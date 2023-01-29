package com.github.davidfantasy.mybatisplus.generatorui.controller;

import com.github.davidfantasy.mybatisplus.generatorui.common.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.dto.MpgGenCodeDto;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.MbpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mbp-generator")
public class MbpGeneratorController {

    @Autowired
    private MbpGenerator mbpGenerator;

    @PostMapping("/gen-code")
    public Result genCode(@RequestBody MpgGenCodeDto dto) {
        mbpGenerator.genCodeBatch(dto.getGenSetting(), dto.getTables());
        return ResultGenerator.genSuccessResult();
    }

}
