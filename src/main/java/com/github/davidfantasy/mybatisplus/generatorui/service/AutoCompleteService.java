package com.github.davidfantasy.mybatisplus.generatorui.service;

import cn.hutool.core.io.FileUtil;
import com.github.davidfantasy.mybatisplus.generatorui.ProjectPathResolver;
import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Set;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Service
public class AutoCompleteService {

    @Autowired
    private ProjectPathResolver projectPathResolver;

    public Set<String> searchXmlMapperName(String mapperLocationPrefix, String searchKey) {
        String mapperRootPath = "";
        if (PACKAGE_RESOURCES_PREFIX.startsWith(mapperLocationPrefix)) {
            mapperRootPath = projectPathResolver.getResourcePath();
        } else if (PACKAGE_JAVA_PREFIX.startsWith(mapperLocationPrefix)) {
            mapperRootPath = projectPathResolver.getSourcePath();
        } else {
            throw new ServiceException("无法识别的源码前缀：" + mapperLocationPrefix);
        }
        Set<String> hitNames = Sets.newHashSet();
        doSearch(new File(mapperRootPath), DOT_XML, searchKey, hitNames);
        return hitNames;
    }

    private void doSearch(File rootDir, String searchKey, String suffix, Set<String> hitNames) {
        if (!FileUtil.exist(rootDir)) {
            return;
        }
        if (!FileUtil.isDirectory(rootDir)) {
            return;
        }
        File[] files = FileUtil.ls(rootDir.getAbsolutePath());
        for (File file : files) {
            if (!file.isDirectory()) {
                String filePackageName = projectPathResolver.convertPathToPackage(file.getAbsolutePath());
                if (match(filePackageName, searchKey, suffix)) {
                    hitNames.add(filePackageName.substring(0, filePackageName.length() - suffix.length()));
                }
            } else {
                doSearch(file, suffix, searchKey, hitNames);
            }
        }
    }

    private boolean match(String name, String searchKey, String suffix) {
        if (!name.endsWith(suffix)) {
            return false;
        }
        if (Strings.isNullOrEmpty(name)) {
            return true;
        }
        return name.toLowerCase().indexOf(searchKey.toLowerCase()) != -1;
    }
}
