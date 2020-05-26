package com.github.davidfantasy.mybatisplus.generatorui.strategy;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import lombok.Data;

@Data
public class MapperStrategy {

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    private String superMapperClass = ConstVal.SUPER_MAPPER_CLASS;

    
}
