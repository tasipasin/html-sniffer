
package com.tasi.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Search Controller singleton class.
 */
public class SearchController {

    /** Only instance of Search Controller. */
    private static SearchController instance;
    /** Result list. */
    private final List<SearchResult> results;

    /**
     * Search Controller singleton class.
     */
    private SearchController() {
        // Singleton class
        this.results = new ArrayList<>();
    }

    /**
     * Execute procedures to begin a new search and returns an identifier
     * of the started search for future consulting.
     * @param keyword Keyword to be searched.
     * @return An ID of the search
     */
    public String beginsNewSearch(String keyword) {
        if (SearchController.validateKeyword(keyword)) {
            // Creates a new SearchResult with a random ID
            SearchResult sr = new SearchResult();
            this.results.add(sr);
            // Creates a thread to keep searching in background
            ThreadPool.getInstance().run(new Search(sr, keyword));
            // Returns the ID
            return sr.getId();
        }
        return null;
    }

    /**
     * Returns the SearchResult object from the ID.
     * @param idString Search ID to be search.
     * @return the SearchResult object from the ID.
     */
    public SearchResult getSearchResultById(String idString) {
        return this.results.stream()
                .filter(result -> Objects.equals(result.getId(), idString))
                .findFirst()
                .orElse(null);
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
     * Returns the instance of SearchController.
     * @return the instance of SearchController.
     */
    public static synchronized SearchController getInstance() {
        if (null == SearchController.instance) {
            SearchController.instance = new SearchController();
        }
        return SearchController.instance;
    }
}
