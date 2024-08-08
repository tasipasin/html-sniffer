
package com.tasi.backend.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SearchResult {

    private final String id;
    private EnumState status;
    private final Set<String> urls = new HashSet<>();

    public SearchResult() {
        this.id = UUID.randomUUID().toString().split("-")[0];
    }

    public String getId() {
        return this.id;
    }

    public EnumState getStatus() {
        return this.status;
    }

    public List<String> getUrls() {
        return this.getUrls();
    }

    public void addUrl(String url) {
        urls.add(url);
    }
}
