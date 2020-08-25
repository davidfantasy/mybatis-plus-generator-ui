package com.github.davidfantasy.mybatisplus.generatorui.util;

import com.google.common.base.Strings;

import java.io.File;

public class PathUtil {

    public static String joinPath(String... paths) {
        StringBuilder tmp = new StringBuilder();
        for (String path : paths) {
            if (!Strings.isNullOrEmpty(path)) {
                tmp.append(path);
                tmp.append(File.separator);
            }
        }
        return tmp.deleteCharAt(tmp.lastIndexOf(File.separator)).toString();
    }

    public static String joinPackage(String... packages) {
        StringBuilder tmp = new StringBuilder();
        for (String aPackage : packages) {
            if (!Strings.isNullOrEmpty(aPackage)) {
                tmp.append(aPackage);
                tmp.append(".");
            }
        }
        return tmp.deleteCharAt(tmp.lastIndexOf(".")).toString();
    }

    public static String getShortNameFromFullRef(String ref) {
        if (Strings.isNullOrEmpty(ref)) {
            return "";
        }
        if (ref.indexOf(".") == -1) {
            return ref;
        }
        return ref.substring(ref.lastIndexOf(".")+1, ref.length());
    }

}
