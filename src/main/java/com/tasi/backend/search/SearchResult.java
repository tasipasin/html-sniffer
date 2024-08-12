
package com.tasi.backend.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Contains the results of the search.
 */
public class SearchResult {

    /** Unique ID for the Search. */
    private final String id;
    /** Current Search Status. */
    private EnumState status;
    /** List of URLS that contains the term searched. */
    private final Set<String> urls = new HashSet<>();

    /**
     * Contains the results of the search.
     */
    public SearchResult() {
        this.id = UUID.randomUUID().toString().split("-")[0];
        this.status = EnumState.ACTIVE;
    }

    /**
     * Returns the Search ID.
     * @return the Search ID.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the Status.
     * @return the Status.
     */
    public EnumState getStatus() {
        return this.status;
    }

    /**
     * Returns the list of URLs the term appears.
     * @return the list of URLs the term appears.
     */
    public List<String> getUrls() {
        return this.getUrls();
    }

    /**
     * Adds a new url to the list.
     * @param url URL to be added.
     */
    public void addUrl(String url) {
        urls.add(url);
    }

    /**
     * Set Done Status.
     */
    public void setDoneStatus() {
        this.status = EnumState.DONE;
    }
}
