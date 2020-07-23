package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.dto.GenSetting;
import com.google.common.collect.Maps;

import java.util.Map;

public class TableInjectionConfig extends InjectionConfig {

    private GeneratorConfig generatorConfig;

    private TemplateVaribleInjecter varibleInjecter;

    private GenSetting genSetting;

    public TableInjectionConfig(GeneratorConfig generatorConfig, GenSetting genSetting) {
        this.generatorConfig = generatorConfig;
        this.varibleInjecter = generatorConfig.getTemplateVaribleInjecter();
        this.genSetting = genSetting;
    }

    @Override
    public void initMap() {
    }

    @Override
    public void initTableMap(TableInfo tableInfo) {
        Map<String, Object> vars = null;
        if (this.varibleInjecter != null) {
            vars = this.varibleInjecter.getCustomTemplateVaribles(tableInfo);
        }
        if (vars == null) {
            vars = Maps.newHashMap();
        }
        if (genSetting.getChoosedControllerMethods() != null) {
            Map<String, Object> controllerMethodsVar = Maps.newHashMap();
            for (String method : genSetting.getChoosedControllerMethods()) {
                controllerMethodsVar.put(method, true);
            }
            if (controllerMethodsVar.size() > 0) {
                controllerMethodsVar.put("hasMethod", true);
            }
            vars.put("controllerMethods", controllerMethodsVar);
        }
        this.setMap(vars);
    }

}
