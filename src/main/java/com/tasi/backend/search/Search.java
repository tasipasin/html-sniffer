
package com.tasi.backend.search;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tasi.backend.utils.HtmlUtils;

/**
 * Class responsible to perform the search.
 */
public class Search implements Runnable {

    /** LOG. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
    /** Default base URL to start looking. */
    private static final String DEFAULT_BASE_URL = "https://www.win-rar.com/";
    /** The actual base URL. */
    private final String baseUrl;
    /** Term to search. */
    private final String term;
    /** Result object. */
    private final SearchResult result;
    /** List of visited links. */
    private final Set<String> linksVisited = new HashSet<>();

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
        long dtBegin = 0;
        // Adds the base url to be the start point
        dtBegin = System.currentTimeMillis();
        LOGGER.debug("Started looking for {}", this.term);
        //////////////////////////////////////////////////
        this.search(this.baseUrl);
        //////////////////////////////////////////////////
        long dtEnd = System.currentTimeMillis();
        LOGGER.info("Search for {} under ID {} is finished on {} milli", this.term, this.result.getId(),
                dtEnd - dtBegin);
        // No more links to visit, the the process is done
        this.result.setDoneStatus();
        // The thread will be terminated
        Thread.currentThread().interrupt();
    }

    /**
     * Process that really executes the search.
     * @param url Url to search.
     */
    private void search(String url) {
        // Executes process to check if the term exists at the page
        // and returns it's html content
        String html = this.checksIfTermExists(url);
        // Deal with the links of the page
        this.collectLinks(html, url);
    }

    /**
     * Executes the search process.
     * @param currVisiting URL being visited.
     * @return Returns the HTML content of the URL.
     */
    private String checksIfTermExists(String currVisiting) {
        String html = "";
        if (!this.hasVisited(currVisiting)) {
            this.addToVisited(currVisiting);
            // Extracts the html
            html = HtmlUtils.getDataContent(currVisiting);
            // Check if the term is present in the page
            if (html.toLowerCase().contains(this.term.toLowerCase())) {
                this.result.addUrl(currVisiting);
            }
        }
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
        for (String href : hrefs) {
            // this.search(href)
            ThreadPool.getInstance().run(() -> search(href));
        }
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

    /**
     * Add a link to visited list.
     * @param value Link visited.
     */
    private synchronized void addToVisited(String value) {
        this.linksVisited.add(value);
    }

    /**
     * Checks if a link has been visited.
     * @param value Link to be checked.
     * @return Indicative of link visited.
     */
    private boolean hasVisited(String value) {
        return this.linksVisited.contains(value);
    }
}
