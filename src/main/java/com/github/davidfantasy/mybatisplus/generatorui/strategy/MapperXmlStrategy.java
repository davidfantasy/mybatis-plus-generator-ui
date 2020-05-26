package com.github.davidfantasy.mybatisplus.generatorui.strategy;

import lombok.Data;

@Data
public class MapperXmlStrategy {


    /**
     * 是否生成baseResultMap
     */
    private boolean baseResultMap = false;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache = false;

}
