
package com.tasi.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchController implements Runnable {

    private final List<SearchResult> results = new ArrayList<>();

    @Override
    public void run() {
        //
    }

    public String beginsNewSearch(String keyword) {
        if (null != keyword && !keyword.isEmpty()) {
            SearchResult sr = new SearchResult();
            this.results.add(sr);
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
