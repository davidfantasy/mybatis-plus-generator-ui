package com.github.davidfantasy.mybatisplus.generatorui;

import lombok.Builder;

@Builder
public class GeneratorConfig {

    private String host = "localhost";

    private Integer port = 8068;

    private TemplateVaribleConfig templateVaribleConfig;


}
