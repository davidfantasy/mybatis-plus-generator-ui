package com.github.davidfantasy.mybatisplus.generatorui;

import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.util.PathUtil;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.*;

@Getter
@Slf4j
public class ProjectPathResolver {

    private String sourcePath;

    private String resourcePath;

    private String baseProjectPath;

    private String basePackage;

    private Pattern packagePattern = Pattern.compile("[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*");

    public ProjectPathResolver(String basePackage) {
        this.basePackage = basePackage;
        String curentThreadPath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
        curentThreadPath = getUTF8String(curentThreadPath);
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
        sourcePath = new File(baseProjectPath + "src/main/java").toString();
        resourcePath = new File(baseProjectPath + "src/main/resources").toString();
    }

    /**
     * 中文文件夹UTF8编码
     *
     * @param basePath
     * @return
     */
    private String getUTF8String(String basePath) {
        try {
            basePath = URLDecoder.decode(basePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return basePath;
    }

    /**
     * 将文件输出的包名转换为绝对路径
     */
    public String convertPackageToPath(String packageName) {
        if (Strings.isNullOrEmpty(packageName)) {
            throw new ServiceException("包名为空");
        }
        boolean isResourceFile = false;
        if (packageName.startsWith(PACKAGE_RESOURCES_PREFIX)) {
            packageName = packageName.replaceFirst(PACKAGE_RESOURCES_PREFIX, "");
            isResourceFile = true;
        } else if (packageName.startsWith(PACKAGE_JAVA_PREFIX)) {
            packageName = packageName.replaceFirst(PACKAGE_JAVA_PREFIX, "");
            isResourceFile = false;
        }
        if (!packagePattern.matcher(packageName).matches()) {
            throw new ServiceException("不是合法的包名：" + packageName);
        }
        String[] folders = packageName.split("\\.");
        String path = sourcePath;
        if (isResourceFile) {
            path = resourcePath;
        }
        for (String folder : folders) {
            path = path + File.separator + folder;
        }
        return path;
    }

    public String resolveEntityPackage() {
        return PathUtil.joinPackage(basePackage, "entity");
    }

    public String resolveControllerPackage() {
        return PathUtil.joinPackage(basePackage, "controller");
    }

    public String resolveServicePackage() {
        return PathUtil.joinPackage(basePackage, "service");
    }

    public String resolveServiceImplPackage() {
        return PathUtil.joinPackage(basePackage, "service", "impl");
    }

    public String resolveMapperPackage() {
        return PathUtil.joinPackage(basePackage, "mapper");
    }

    public String resolveMapperXmlPackage() {
        return PACKAGE_RESOURCES_PREFIX + "mapper";
    }

}
