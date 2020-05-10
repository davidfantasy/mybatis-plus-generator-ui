package com.github.davidfantasy.mybatisplus.generatorui;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeneratorConfig {

    private String host = "localhost";

    private Integer port = 8068;

    private String jdbcUrl;

    private String userName;

    private String password;

    private TemplateVaribleConfig templateVaribleConfig;


}
