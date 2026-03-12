package com.example.weather.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * JSON工具类
 */
@Component
public class SIJsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(SIJsonUtil.class);

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json  JSON字符串
     * @param clazz 目标类
     * @param <T>   泛型
     * @return 对象
     */
    public <T> T parseObject(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            logger.error("JSON解析失败", e);
            return null;
        }
    }

    /**
     * 将JSON字符串转换为JSONObject
     *
     * @param json JSON字符串
     * @return JSONObject
     */
    public JSONObject parseObject(String json) {
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            logger.error("JSON解析失败", e);
            return null;
        }
    }
}
