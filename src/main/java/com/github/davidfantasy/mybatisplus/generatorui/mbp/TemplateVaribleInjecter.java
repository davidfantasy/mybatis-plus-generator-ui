package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.Map;

public interface TemplateVaribleInjecter {


    Map<String, Object> getCustomTemplateVaribles(TableInfo tableInfo);


}
