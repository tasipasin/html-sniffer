
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
    /** The actual base URL. */
    private final String baseUrl;
    /** Term to search. */
    private final String term;
    /** Result object. */
    private final SearchResult result;
    private final Set<String> linksVisited = new HashSet<>();
    private final Queue<String> linksToVisit = new ConcurrentLinkedQueue<>();

    /**
     * Class responsible to perform the search.
     * @param result Result Object created previously.
     * @param term Term to search.
     */
    public Search(SearchResult result, String term) {
        this.result = result;
        this.term = term;
        this.baseUrl = Search.getBase();
    }

    @Override
    public void run() {
        // Adds the base url to be the start point
        linksToVisit.add(this.baseUrl);
        LOGGER.debug("Started looking for {}", this.term);
        while (!linksToVisit.isEmpty()) {
            // Gets the next url to visit
            String currVisiting = linksToVisit.poll();
            // Executes process to check if the term exists at the page
            // and returns it's html content
            String html = this.checksIfTermExists(currVisiting);
            // Deal with the links of the page
            this.collectLinks(html, currVisiting);
            LOGGER.info("Remaining {} links to visit", linksToVisit.size());
        }
        LOGGER.info("Search for {} under ID {} is finished", this.term, this.result.getId());
        // No more links to visit, the the process is done
        this.result.setDoneStatus();
        // The thread will be terminated
        Thread.currentThread().interrupt();
    }

    /**
     * Executes the search process.
     * @param currVisiting URL being visited.
     * @return Returns the HTML content of the URL.
     */
    private String checksIfTermExists(String currVisiting) {
        LOGGER.debug("Visiting link: {}", currVisiting);
        // Extracts the html
        String html = HtmlUtils.getDataContent(currVisiting);
        // Check if the term is present in the page
        if (html.toLowerCase().contains(this.term.toLowerCase())) {
            this.result.addUrl(currVisiting);
            LOGGER.debug("Found keyword {} in {}", this.term, currVisiting);
        }
        linksVisited.add(currVisiting);
        return html;
    }

    /**
     * Collect all links in the html content.
     * @param html HTML content.
     * @param currVisiting URL that is being visited.
     */
    private void collectLinks(String html, String currVisiting) {
        // Get the filtered and normalized hrefs from the html
        Set<String> hrefs = HtmlUtils.getFilteredAndNormalizedHrefs(html, currVisiting);
        hrefs.removeAll(this.linksVisited);
        // Just to log, since Set already deals with duplicated entries
        hrefs.removeAll(this.linksToVisit);
        LOGGER.debug("{} new links to visit: {}", hrefs.size(), hrefs);
        // Adds to linksToVisit
        linksToVisit.addAll(hrefs);
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
