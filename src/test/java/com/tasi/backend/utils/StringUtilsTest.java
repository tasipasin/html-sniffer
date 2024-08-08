
package com.tasi.backend.utils;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

    /**
     * Check if the required string is successfully parsed as a Map
     */
    @Test
    void parseStringToMap() {
        String origin = "{\"keyword\":\"tasi\"}";
        Map<String, String> result = StringUtils.stringAsMap(origin);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(!result.isEmpty());
        Assertions.assertTrue(result.containsKey("keyword"));
        Assertions.assertEquals("tasi", result.get("keyword"));
    }

    /**
     * Check if the required string, a json string with a number value,
     * is successfully parsed as a Map.
     */
    @Test
    void parseStringToMapNumberValue() {
        String origin = "{\"keyword\":\"123\"}";
        Map<String, String> result = StringUtils.stringAsMap(origin);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(!result.isEmpty());
        Assertions.assertTrue(result.containsKey("keyword"));
        Assertions.assertEquals("123", result.get("keyword"));
    }
}
