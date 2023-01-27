package com.github.davidfantasy.mybatisplus.generatorui;

import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.util.OSUtil;
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

    private final String basePackage;

    private final Pattern packagePattern = Pattern.compile("[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*");

    public ProjectPathResolver(String basePackage) {
        this.basePackage = basePackage;
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        String rootPath = "";
        if (contextLoader.getResource(".") != null) {
            String projectDir = contextLoader.getResource(".").getPath();
            projectDir = getUTF8String(projectDir);
            String[] paths = projectDir.split("/");
            StringBuilder temp = new StringBuilder();
            //去掉目录的最后两个子目录，通常是/target/classes
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                if (Strings.isNullOrEmpty(path)) {
                    continue;
                }
                if (i < paths.length - 2) {
                    temp.append(path);
                    temp.append(File.separator);
                }
            }
            rootPath = temp.toString();
        } else {
            rootPath = getUTF8String(System.getProperty("user.dir")) + File.separator;
        }
        //linux环境下识别项目根目录缺少“/”的问题
        if (!OSUtil.isWindows() && !rootPath.startsWith("/")) {
            rootPath = "/" + rootPath;
        }
        refreshBaseProjectPath(rootPath);
    }

    /**
     * 中文文件夹UTF8编码
     *
     * @param basePath
     * @return
     */
    public String getUTF8String(String basePath) {
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
        }
        if (!packagePattern.matcher(packageName).matches()) {
            throw new ServiceException("不是合法的包名：" + packageName);
        }
        String[] folders = packageName.split("\\.");
        StringBuilder path = new StringBuilder(sourcePath);
        if (isResourceFile) {
            path = new StringBuilder(resourcePath);
        }
        for (String folder : folders) {
            path.append(File.separator).append(folder);
        }
        return path.toString();
    }

    public String convertPathToPackage(String path) {
        if (path.startsWith(sourcePath)) {
            path = path.replace(sourcePath, "");
        } else if (path.startsWith(resourcePath)) {
            path = path.replace(resourcePath, "");
        } else {
            throw new ServiceException("无法将该路径转换为包名：" + path);
        }
        String packageStr = path.replace(File.separator, ".");
        if (packageStr.startsWith(".")) {
            packageStr = packageStr.substring(1, packageStr.length());
        }
        return packageStr;
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

    public synchronized void refreshBaseProjectPath(String rootPath) {
        if (baseProjectPath == null || !baseProjectPath.equals(rootPath)) {
            this.baseProjectPath = rootPath;
            sourcePath = new File(baseProjectPath + "src/main/java".replace("/", File.separator)).toString();
            resourcePath = new File(baseProjectPath + "src/main/resources".replace("/", File.separator)).toString();
        }
    }

}
