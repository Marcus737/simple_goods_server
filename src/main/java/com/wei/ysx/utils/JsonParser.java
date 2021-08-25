package com.wei.ysx.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wei.ysx.entity.Goods;




public class JsonParser {


    static ObjectMapper pm = new ObjectMapper();

    private static final String MAIN_BODY = "showapi_res_body";

    public static <T> T parseGoods(String json, Class<T> tClass) throws JsonProcessingException {
        //忽略不存在的字段
        pm.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = pm.readValue(json, JsonNode.class);
        json = jsonNode.get(MAIN_BODY).toString();
        T t  = pm.readValue(json, tClass);
        return t;
    }

}
