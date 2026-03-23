package com.sportsvista.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SafeParser {

    public static String str(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull() && obj.get(key).isJsonPrimitive()) {
            return obj.get(key).getAsString();
        }
        return "";
    }

    public static int Int(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                return obj.get(key).getAsInt();
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static double dbl(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                return obj.get(key).getAsDouble();
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    public static boolean bool(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) {
            try {
                return obj.get(key).getAsBoolean();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static JsonObject obj(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && obj.get(key).isJsonObject()) {
            return obj.get(key).getAsJsonObject();
        }
        return null;
    }

    public static JsonArray arr(JsonObject obj, String key) {
        if (obj != null && obj.has(key) && obj.get(key).isJsonArray()) {
            return obj.get(key).getAsJsonArray();
        }
        return new JsonArray();
    }
}
