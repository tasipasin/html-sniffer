
package com.tasi.backend.utils;

import com.google.gson.Gson;
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
     * Convert an object to a Json String.
     * @param obj Object to be converted.
     * @return Object's Json String.
     */
    public static String objectToString(Object obj) {
        if (null == obj) {
            return "";
        }
        return StringUtils.GSON.toJson(obj);
    }
}
