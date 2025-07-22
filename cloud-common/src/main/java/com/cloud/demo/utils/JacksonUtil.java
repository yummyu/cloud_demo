package com.cloud.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author DeepSeek
 * Jackson JSON 工具类
 * 线程安全配置 + 常用方法封装
 */
public class JacksonUtil {

    // 使用静态final保证线程安全 (ObjectMapper线程安全配置后可在多线程环境下复用)
    private static final ObjectMapper MAPPER = createObjectMapper();

    // 配置基础ObjectMapper (按需修改配置)
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 注册Java8时间模块
        mapper.registerModule(new JavaTimeModule());

        // 序列化配置
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 不序列化null字段
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 统一日期格式

        // 反序列化配置
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 忽略未知属性

        return mapper;
    }

    // ------------------- 基础转换方法 -------------------

    /**
     * 对象转JSON字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to JSON error", e);
        }
    }

    /**
     * JSON字符串转对象
     * @param json JSON字符串
     * @param clazz 目标类型
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to object error", e);
        }
    }

    /**
     * JSON转复杂类型 (如List/Map/嵌套对象等)
     * @param json JSON字符串
     * @param typeReference 类型引用 (e.g. new TypeReference<List<User>>(){})
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to complex type error", e);
        }
    }

    // ------------------- 集合转换快捷方法 -------------------

    /**
     * JSON转List
     * @param json JSON字符串
     * @param elementType List元素类型
     */
    public static <T> List<T> toList(String json, Class<T> elementType) {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
        try {
            return MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to List error", e);
        }
    }

    /**
     * JSON转Map
     * @param json JSON字符串
     * @param keyType Key类型
     * @param valueType Value类型
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        try {
            return MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to Map error", e);
        }
    }

    // ------------------- 其他实用方法 -------------------

    /**
     * 对象深拷贝 (通过JSON序列化实现)
     */
    public static <T> T deepCopy(T obj, Class<T> clazz) {
        return fromJson(toJsonString(obj), clazz);
    }

    /**
     * 格式化输出JSON (美化打印)
     */
    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to pretty JSON error", e);
        }
    }
}
