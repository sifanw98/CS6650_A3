package com.albumstore.util;

import com.google.gson.Gson;

public class JSONUtility {
    
    private static Gson gson = new Gson();

    public static String toJSON(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJSON(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
