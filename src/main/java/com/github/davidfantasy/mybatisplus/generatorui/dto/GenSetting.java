package com.github.davidfantasy.mybatisplus.generatorui.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenSetting {

    /**
     * 需要生成的文件类型
     */
    private List<String> choosedOutputFiles;

    /**
     * 文件存在时是否覆盖
     */
    private boolean override;

    /**
     * 注释的作者
     */
    private String author;

    /**
     * 功能模块名
     */
    private String moduleName;

}
