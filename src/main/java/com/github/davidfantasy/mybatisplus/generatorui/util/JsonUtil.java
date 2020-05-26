package com.github.davidfantasy.mybatisplus.generatorui.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String obj2json(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("将对象转换为JSON时发生错误！", e);
        }
    }

    public static <T> T json2obj(String jsonStr, Class<T> clazz) {
        try {
            return mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + jsonStr, e);
        }
    }

    public static <T> T json2obj(String jsonStr, TypeReference<T> clazzType) {
        try {
            return mapper.readValue(jsonStr, clazzType);
        } catch (IOException e) {
            throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + jsonStr, e);
        }
    }

    public static <T> T json2obj(String content, String path, Class<T> clazz) throws IOException {
        if (!Strings.isNullOrEmpty(path)) {
            JsonNode node = mapper.readTree(content);
            String[] pathes = path.split("\\.");
            for (String p : pathes) {
                node = node.get(p);
            }
            content = node.toString();
        }
        return json2obj(content, clazz);
    }

    public static <T> T json2obj(String content, String path, TypeReference<T> clazzType) throws IOException {
        if (!Strings.isNullOrEmpty(path)) {
            JsonNode node = mapper.readTree(content);
            String[] pathes = path.split("\\.");
            for (String p : pathes) {
                node = node.get(p);
            }
            content = node.toString();
        }
        return json2obj(content, clazzType);
    }


}
