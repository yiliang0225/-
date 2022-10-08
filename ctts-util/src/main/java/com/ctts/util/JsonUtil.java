package com.ctts.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author liangbaichuan
 */
public class JsonUtil {
    /**
     * 将对象转换为Json字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonStr(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 获取JsonObject
     *
     * @param jsonStr
     * @return
     */
    public static JsonObject parseJsonAsObject(String jsonStr) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = null;
        try {
            jsonObj = parser.parse(jsonStr).getAsJsonObject();
        } catch (Exception e) {
        }
        return jsonObj;
    }

    public static JsonArray parseJsonAsArray(String jsonStr) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;
        try {
            jsonArray = parser.parse(jsonStr).getAsJsonArray();
        } catch (Exception e) {
        }
        return jsonArray;
    }

    /**
     * 根据json字符串返回Map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> toMap(String jsonStr) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (jsonStr != null) {
            jsonMap = JsonUtil.toMap(JsonUtil.parseJsonAsObject(jsonStr));
        }
        return jsonMap;
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (json != null) {
            map = new HashMap<String, Object>();
            Set<Entry<String, JsonElement>> entrySet = json.entrySet();
            for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter
                    .hasNext(); ) {
                Entry<String, JsonElement> entry = iter.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof JsonArray) {
                    map.put((String) key, toList((JsonArray) value));
                } else if (value instanceof JsonObject) {
                    map.put((String) key, toMap((JsonObject) value));
                } else {
                    map.put(key, value != null ? value.toString().replaceAll("^\"|\"$", "") : value);
                }
            }
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }

    public static List<Object> toList(String jsonStr) {
        return toList(parseJsonAsArray(jsonStr));
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> toListMap(String jsonStr) {
        if (StringUtil.isEmptyStr(jsonStr)) {
            return null;
        }
        List<Object> lo = toList(jsonStr);
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        for (Object o : lo) {
            lm.add((Map<String, Object>) o);
        }
        return lm;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> toListMapOfString(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        List<Object> lo = toList(jsonStr);
        List<Map<String, String>> lm = new ArrayList<Map<String, String>>();
        for (Object o : lo) {
            lm.add((Map<String, String>) o);
        }
        return lm;
    }

    public static List<Map<String, Object>> toListMapByJackson(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(s, List.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 可解析json中的Integer和Double
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> toMapByJackson(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toObject(String src, Class<T> returnType) {
        Gson gson = new Gson();
        T t = gson.fromJson(src, returnType);
        return t;
    }

}
