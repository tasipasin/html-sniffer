
package com.tasi.backend;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonObject;
import com.tasi.backend.search.SearchController;
import com.tasi.backend.search.SearchResult;
import com.tasi.backend.utils.StringUtils;
import spark.Request;
import spark.Response;

/**
 * Class to perform request verifications and assign the work to the best fit.
 */
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    /**
     * Checks the request for a new search and returns the Search ID.
     * @param request The Request.
     * @param res The Result Object.
     * @return Returns the answer to the request. Expected to be an ID.
     */
    public JsonObject postRequest(Request request, Response res) {
        res.type("application/json");
        JsonObject result = null;
        // Get the keyword and validates it
        String keyword = getParameter(request, "keyword");
        // Tells the search controller to start a new search and
        // returns an ID of the search
        String id = SearchController.getInstance().beginsNewSearch(keyword);
        if (null != id) {
            result = new JsonObject();
            result.addProperty("id", id);
        }
        // If the keyword is invalid or not passed, returns a Bad Request
        if (null == result) {
            res.status(HttpStatus.BAD_REQUEST_400);
            result = Controller.makeError(HttpStatus.BAD_REQUEST_400,
                    "field \'keyword\' is required (from 4 up to 32 chars)");
        }
        LOGGER.info("Search request for keyword [{}] ended with ID [{}]", keyword, id);
        return result;
    }

    /**
     * Checks the request to get the Search Result from a given ID.
     * @param request Request.
     * @param res Response.
     * @return Returns the current Search Result.
     */
    public JsonObject getRequest(Request request, Response res) {
        res.type("application/json");
        JsonObject result = null;
        // Gets the parameter as ID
        String id = request.params("id");
        // Returns the Search Result of the ID
        SearchResult sr = SearchController.getInstance().getSearchResultById(id);
        if (sr != null) {
            result = StringUtils.objectToJsonObject(sr);
        }
        if (null == result) {
            res.status(HttpStatus.BAD_REQUEST_400);
            result = Controller.makeError(HttpStatus.BAD_REQUEST_400, String.format("crawl not found: %s", id));
        }
        return result;
    }

    /**
     * Makes a layout default error message.
     * @param code Error code.
     * @param message Error message.
     * @return A JsonObject with the error.
     */
    private static JsonObject makeError(int code, String message) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", code);
        jsonObj.addProperty("message", message);
        return jsonObj;
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
