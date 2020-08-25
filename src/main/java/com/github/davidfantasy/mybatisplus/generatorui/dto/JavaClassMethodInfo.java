package com.github.davidfantasy.mybatisplus.generatorui.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class JavaClassMethodInfo {

    private String classRef;

    private String methodName;

    private String returnType;

    private String comments;

    private List<DtoFieldInfo> params;

    private Set<String> importJavaTypes;

}
