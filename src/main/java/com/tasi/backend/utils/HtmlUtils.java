
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
        String content = null;
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
        linkList.removeIf(link -> !link.startsWith(domain) && link.contains("http"));
    }
}
