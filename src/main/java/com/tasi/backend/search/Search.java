
package com.tasi.backend.search;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tasi.backend.utils.HtmlUtils;

/**
 * Class responsible to perform the search.
 */
public class Search implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
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
        LOGGER.debug("Started looking for {}", this.term);
        while (!linksToVisit.isEmpty()) {
            // Gets the next url to visit
            String currVisiting = linksToVisit.poll();
            LOGGER.debug("Visiting link: {}", currVisiting);
            // Extracts the html
            String html = HtmlUtils.getDataContent(currVisiting);
            // Check if the term is present in the page
            if (html.toLowerCase().contains(this.term.toLowerCase())) {
                this.result.addUrl(currVisiting);
                LOGGER.debug("Found keyword {} in {}", this.term, currVisiting);
            }
            linksVisited.add(currVisiting);
            // Get the filtered and normalized hrefs from the html
            Set<String> hrefs = HtmlUtils.getFilteredAndNormalizedHrefs(html, DEFAULT_BASE_URL);
            hrefs.removeAll(linksVisited);
            // Just to log, since Set already deals with duplicated entries
            hrefs.removeAll(linksToVisit);
            LOGGER.debug("{} new links to visit: {}", hrefs.size(), hrefs);
            // Adds to linksToVisit
            linksToVisit.addAll(hrefs);
            LOGGER.info("Remaining {} links to visit", linksToVisit.size());
        }
        LOGGER.info("Search for {} under ID {} is finished", this.term, this.result.getId());
        // No more links to visit, the the process is done
        this.result.setDoneStatus();
        // The thread will be terminated
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
            LOGGER.info("Base URL not found in Environment Variables");
        }
        LOGGER.info("Base URL to be used: {}", envResult);
        return envResult;
    }
}
