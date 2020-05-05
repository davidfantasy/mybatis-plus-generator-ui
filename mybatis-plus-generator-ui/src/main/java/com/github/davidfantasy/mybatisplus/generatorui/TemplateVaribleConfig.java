package com.github.davidfantasy.mybatisplus.generatorui;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.Map;

public interface TemplateVaribleConfig {


    Map<String, String> getCustomTemplateVaribles(TableInfo tableInfo);


}
