
package com.tasi.backend.search;

import com.tasi.backend.utils.HtmlUtils;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class responsible to perform the search.
 */
public class Search implements Runnable {

    /** Default base URL to start looking. */
    private static final String DEFAULT_BASE_URL = "https://www.win-rar.com/";
    /** Term to search. */
    private final String term;
    /** Result object. */
    private final SearchResult result;

    /**
     * Class responsible to perform the search.
     * @param result Result Object created previously.
     * @param term Term to search.
     */
    public Search(SearchResult result, String term) {
        this.result = result;
        this.term = term;
    }

    @Override
    public void run() {
        Set<String> linksVisited = new HashSet<>();
        Queue<String> linksToVisit = new ConcurrentLinkedQueue<>();
        // Adds the base url to be the start point
        linksToVisit.add(Search.getBase());
        while (!linksToVisit.isEmpty()) {
            // Gets the next url to visit
            String currVisiting = linksToVisit.poll();
            // Extracts the html
            String html = HtmlUtils.getDataContent(currVisiting);
            // Check if the term is present in the page
            if (html.contains(this.term)) {
                this.result.addUrl(currVisiting);
            }
            linksVisited.add(currVisiting);
            // Get the filtered and normalized hrefs from the html
            Set<String> hrefs = HtmlUtils.getFilteredAndNormalizedHrefs(html, DEFAULT_BASE_URL);
            hrefs.removeAll(linksVisited);
            // Just to log, since Set already deals with duplicated entries
            hrefs.removeAll(linksToVisit);
            // Adds to linksToVisit
            linksToVisit.addAll(hrefs);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // No more links to visit, the the process is done
        this.result.setDoneStatus();
        // The thread is killed
        Thread.currentThread().interrupt();
    }

    /**
     * Gets enviroment value for the Base URL
     * @return The Base URL parameterized.
     */
    private static String getBase() {
        String envResult = System.getenv("BASE_URL");
        // If not configured, returns the default base url
        if (null == envResult || envResult.trim().isEmpty()) {
            envResult = DEFAULT_BASE_URL;
        }
        return envResult;
    }
}
