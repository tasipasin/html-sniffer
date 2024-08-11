
package com.tasi.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Search Controller class.
 */
public class SearchController {

    /** Result list. */
    private final List<SearchResult> results = new ArrayList<>();

    /**
     * Execute procedures to begin a new search and returns an identifier
     * of the started search for future consulting.
     * @param keyword Keyword to be searched.
     * @return An ID of the search
     */
    public String beginsNewSearch(String keyword) {
        if (null != keyword) {
            // Creates a new SearchResult with a random ID
            SearchResult sr = new SearchResult();
            this.results.add(sr);
            // Creates a thread to keep searching in background
            Thread searchThread = new Thread(new Search(sr, keyword));
            searchThread.start();
            // Returns the ID
            return sr.getId();
        }
        return null;
    }

    /**
     * Returns the SearchResult object
     * @param idString
     * @return
     */
    public SearchResult getSearchResultById(String idString) {
        return this.results.stream()
                .filter(result -> Objects.equals(result.getId(), idString))
                .findFirst()
                .orElse(null);
    }
}
