package com.github.davidfantasy.mybatisplus.generatorui.service;

import com.github.davidfantasy.mybatisplus.generatorui.MybatisPlusToolsApplication;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class TestService {

    @Autowired
    private ProjectPathResolver pathResolver;

    @PostConstruct
    public void test() {
        log.info("project location:{}", pathResolver.getBaseProjectPath());
    }

}
