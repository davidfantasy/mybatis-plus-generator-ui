package com.github.davidfantasy.mybatisplus.generatorui.mbp;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;

public class TableInjectionConfig extends InjectionConfig {

    private GeneratorConfig generatorConfig;

    private TemplateVaribleInjecter varibleInjecter;

    public TableInjectionConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
        this.varibleInjecter = generatorConfig.getTemplateVaribleInjecter();
    }

    @Override
    public void initMap() {
    }

    @Override
    public void initTableMap(TableInfo tableInfo) {
        if (this.varibleInjecter != null) {
            this.setMap(this.varibleInjecter.getCustomTemplateVaribles(tableInfo));
        } else {
            this.setMap(null);
        }
    }

}
