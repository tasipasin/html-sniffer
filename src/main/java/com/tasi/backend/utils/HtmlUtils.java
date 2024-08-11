
package com.tasi.backend.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * Utility class to deal with Html types.
 */
public class HtmlUtils {

    private HtmlUtils() {
        // Utility classes should not have public constructors
        // squid: S1118
    }

    /**
     * Return the page HTML content.
     * @param url URL to extract the HTML content.
     * @return the page HTML content.
     */
    public static String getDataContent(String url) {
        String content = "";
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
            connection.getRequestProperties();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    /**
     * Returns all the href at anchor tag in the HTML structure.
     * @param html HTML structure.
     * @return List of all the links found at the HTML structure.
     */
    public static Set<String> getLinksInContent(String html) {
        Reader reader = new StringReader(html);
        HTMLEditorKit.Parser parser = new ParserDelegator();
        Set<String> links = new HashSet<>();
        try {
            parser.parse(reader, new HTMLEditorKit.ParserCallback() {

                @Override
                public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
                    if (t == HTML.Tag.A) {
                        Object link = a.getAttribute(HTML.Attribute.HREF);
                        if (link != null) {
                            links.add(String.valueOf(link));
                        }
                    }
                }
            }, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return links;
    }

    /**
     * Given a list of links, filter those relative or absolute to the domain.
     * @param linkList List of links.
     * @param domain Root domain.
     */
    public static void filterSameDomainLinks(Set<String> linkList, String domain) {
        // Removes any links that:
        // 1.   Does not ends with .html extension OR
        // 2.   Do not starts with the required domain AND
        // 2.1. Contains http OR
        // 2.2. Do not starts with a single slash
        linkList.removeIf(link -> !link.endsWith(".html") || !link.startsWith(domain)
                && (link.contains("http") || !link.startsWith("/")));
    }

    /**
     * Given a list of links, for each relative link appends the domain to it.
     * @param linkList List of links.
     * @param domain The domains.
     * @return A normalized list of links
     */
    public static Set<String> addDomainInRelativeLinks(Set<String> linkList, String domain) {
        Set<String> result = new HashSet<>();
        // Iterate over the whole list checking if is a relative link to concanate de domain
        // and puts all links in a return list
        linkList.forEach(link -> {
            String resultLink = link;
            if (!resultLink.contains("http")) {
                resultLink = (domain + resultLink).replaceAll("(?<!:)/+", "/");
            }
            result.add(resultLink);
        });
        return result;
    }

    /**
     * Given a HTML String, all the links of the same origin are returned normalized
     * if they're relative.
     * @param html Html String.
     * @param domain Root Domain.
     * @return A list of links from the current HTML normalized by the domain.
     */
    public static Set<String> getFilteredAndNormalizedHrefs(String html, String domain) {
        Set<String> originalHrefs = getLinksInContent(html);
        HtmlUtils.filterSameDomainLinks(originalHrefs, domain);
        return HtmlUtils.addDomainInRelativeLinks(originalHrefs, domain);
    }
}
