
package com.tasi.backend.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Map;

/**
 * Utility class to deal with strings.
 */
public class StringUtils {

    /** Objecto to deal with JSON. */
    private static final Gson GSON = new Gson();

    private StringUtils() {
        // Utility classes should not have public constructors
        // squid: S1118
    }

    /**
     * Convert a Json String to Map.
     * @param origin Json as String.
     * @return The Json String as Map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> stringAsMap(String origin) {
        return StringUtils.GSON.fromJson(origin, Map.class);
    }

    /**
     * Convert an object to a JsonObject.
     * @param obj Object to be converted.
     * @return A JsonObject of the Object.
     */
    public static JsonObject objectToJsonObject(Object obj) {
        return (JsonObject) GSON.toJsonTree(obj);
    }
}
