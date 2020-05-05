package com.github.davidfantasy.mybatisplus.generatorui;

public class ProjectPathResolver {

    private String sourcePath;

    private String resourcePath;

    public ProjectPathResolver(Class<?> primarySource) {
        String classLocation = primarySource.getProtectionDomain().getCodeSource().getLocation().getPath();

    }
}
