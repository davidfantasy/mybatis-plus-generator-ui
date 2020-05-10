package com.github.davidfantasy.mybatisplus.generatorui;

import lombok.Getter;

import java.net.URL;

@Getter
public class ProjectPathResolver {

    private String sourcePath;

    private String resourcePath;

    private String baseProjectPath;

    public ProjectPathResolver() {
        String curentThreadPath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
        String[] paths = curentThreadPath.split("/");
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (i < paths.length - 2) {
                temp.append(path);
                temp.append("/");
            }
        }
        baseProjectPath = temp.toString();
        sourcePath = baseProjectPath + "src/main/java";
        resourcePath = baseProjectPath + "src/main/resources";
    }
}
