package com.github.davidfantasy.mybatisplus.generatorui.common.dto;

import lombok.Data;

@Data
public class GenDtoFromSqlReq {

    private String sql;

    private GenDtoConfig config = new GenDtoConfig();

}
