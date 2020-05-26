package com.github.davidfantasy.mybatisplus.generatorui.strategy;

import lombok.Data;

import java.util.List;

@Data
public class EntityStrategy {

    /**
     * 自定义继承的Entity类全称，带包名
     */
    private String superEntityClass;

    /**
     * 自定义基础的Entity类，公共字段
     */
    private List<String> superEntityColumns;

    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean entitySerialVersionUID = true;
    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private boolean entityColumnConstant = false;
    /**
     * 【实体】是否为构建者模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     */
    private boolean entityBuilderModel = false;
    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private boolean entityLombokModel = false;
    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean entityBooleanColumnRemoveIsPrefix = false;

    /**
     * 是否生成实体时，生成字段注解
     */
    private boolean entityTableFieldAnnotationEnable = false;

    /**
     * 乐观锁属性名称
     */
    private String versionFieldName;

    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName;

    /**
     * 开启 swagger2 支持
     */
    private boolean swagger2 = false;

}
