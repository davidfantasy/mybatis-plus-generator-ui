package com.github.davidfantasy.mybatisplus.generatorui.controller;

import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.Result;
import com.github.davidfantasy.mybatisplus.generatorui.common.ResultGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.service.AutoCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/ac")
public class AutoCompleteController {

    @Autowired
    private AutoCompleteService autoCompleteService;

    @Autowired
    private ProjectPathResolver projectPathResolver;

    @GetMapping("/mapperxml")
    public Result getAllMapperXmlNames(String mapperLocationPrefix, String searchKey) {
        Set<String> hits = autoCompleteService.searchXmlMapperName(mapperLocationPrefix, searchKey);
        return ResultGenerator.genSuccessResult(hits);
    }

}
