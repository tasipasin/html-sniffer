
package com.tasi.backend;

import org.eclipse.jetty.http.HttpStatus;
import com.tasi.backend.search.SearchController;
import com.tasi.backend.search.SearchResult;
import com.tasi.backend.utils.StringUtils;
import spark.Request;
import spark.Response;

/**
 * Class to perform request verifications and assign the work to the best fit.
 */
public class Controller {

    /** Search Controller. */
    private final SearchController search = new SearchController();
    /** Answer result String template. */
    private static final String RESULT_TEMPLATE = "{\"id\": %s}";

    /**
     * Checks the request for a new search and returns the Search ID.
     * @param request The Request.
     * @param res The Result Object.
     * @return Returns the answer to the request. Expected to be an ID.
     */
    public String postRequest(Request request, Response res) {
        String result = "";
        // Get the keyword and validates it
        String keyword = getParameter(request, "keyword");
        if (Controller.validateKeyword(keyword)) {
            // Tells the search controller to start a new search and
            // returns an ID of the search
            String id = search.beginsNewSearch(keyword);
            if (null != id) {
                result = String.format(RESULT_TEMPLATE, id);
            }
        }
        // If the keyword is invalid or not passed, returns a Bad Request
        if (result.trim().isEmpty()) {
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return result;
    }

    /**
     * Checks the request to get the Search Result from a given ID.
     * @param request Request.
     * @param res Response.
     * @return Returns the current Search Result.
     */
    public String getRequest(Request request, Response res) {
        SearchResult result = null;
        // Gets the parameter as ID
        String id = request.params("id");
        if (null != id) {
            // Returns the Search Result of the ID
            result = search.getSearchResultById(id);
        }
        if (null == result) {
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return StringUtils.objectToString(result);
    }

    /**
     * Validates if the keyword request for searching has length
     * between 4 and 32, included.
     * @param keyword The keyword.
     * @return The keyword validation.
     */
    private static boolean validateKeyword(String keyword) {
        return null != keyword && keyword.length() >= 4 && keyword.length() <= 32;
    }

    /**
     * Get some parameter from Request Object.
     * @param request Request Object.
     * @param key Object key.
     * @return The parameter value, if exists.
     */
    private static String getParameter(Request request, String key) {
        return StringUtils.stringAsMap(request.body()).get(key);
    }
}
