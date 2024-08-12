
package com.tasi.backend.utils;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlUtilsTest {

    private static final String ORIGIN = "https://www.win-rar.com/";

    @Test
    void getHtmlContent() {
        String content = HtmlUtils.getDataContent(ORIGIN);
        Assertions.assertNotNull(content.toLowerCase());
    }

    @Test
    void getAnchorHrefs() {
        String content = HtmlUtils.getDataContent(ORIGIN);
        Set<String> hrefs = HtmlUtils.getLinksInContent(content);
        HtmlUtils.filterSameDomainLinks(hrefs, ORIGIN);
        Assertions.assertTrue(!hrefs.isEmpty());
    }

    @Test
    void filterDomain() {
        Set<String> hrefs = new HashSet<>();
        hrefs.add("https://www.capterra.com/p/207190/WinRAR/");
        hrefs.add("https://www.youtube.com/WinRAR-RARLAB");
        // This must be keeped
        hrefs.add("/windows-11-support-statement.html");
        hrefs.add("https://www.softlay.com/downloads/winrar");
        hrefs.add("https://maddownload.com/utilities/file-compression/winrar");
        // This must be keeped
        hrefs.add("https://www.win-rar.com/industry.html");
        // This must be keeped
        hrefs.add("/predownload.html");
        hrefs.add("https://www.facebook.com/winrar/");
        // This must be keeped
        hrefs.add("/cookies.html");
        // This must be keeped
        hrefs.add("/industry.html");
        hrefs.add("https://winrar.informer.com/");
        hrefs.add("/predownload.html?&Version=32bit");
        hrefs.add("https://www.moosoft.com/software/winrar/");
        // This must be keeped
        hrefs.add("/start.html");
        // This must be keeped
        hrefs.add("/features.html");
        hrefs.add("https://www.twitter.com/winrar_rarlab/");
        hrefs.add("https://www.windows11downloads.com/");
        hrefs.add("https://www.apkmonk.com/posts/winrar-software-review/");
        // This must be keeped
        hrefs.add("/contact.html");
        hrefs.add("https://shop.win-rar.com/16/purl-shop-2183-1-n?x-source=31-buylink-startpage");
        // This must be keeped
        hrefs.add("/windows-10-support-statement.html");
        // This must be keeped
        hrefs.add("https://www.win-rar.com/products-winrar.html");
        // This must be keeped
        hrefs.add("https://www.win-rar.com/download.html");
        hrefs.add("javascript:gaOptin();");
        // This must be keeped
        hrefs.add("https://www.win-rar.com/support.html");
        hrefs.add("https://graphicsfamily.com/winrar/");
        hrefs.add("https://shop.win-rar.com/16/purl-shop-2183-1-n?x-source=31-buybutton-startpage");
        // This must be keeped
        hrefs.add("https://www.win-rar.com/partners.html");
        // This must be keeped
        hrefs.add("https://www.win-rar.com/news.html");
        HtmlUtils.filterSameDomainLinks(hrefs, ORIGIN);
        Assertions.assertFalse(hrefs.isEmpty());
        Assertions.assertEquals(14, hrefs.size());
    }

    @Test
    void normalizeLinks() {
        Set<String> hrefs = new HashSet<>();
        // This must stay after filtering
        hrefs.add("/features.html");
        Set<String> normalized = HtmlUtils.addDomainInRelativeLinks(hrefs, ORIGIN);
        Assertions.assertFalse(normalized.isEmpty());
        Assertions.assertEquals(ORIGIN + "features.html", normalized.iterator().next());
    }

    @Test
    void normalizeLinksHtml() {
        Set<String> hrefs = new HashSet<>();
        // This must stay after filtering
        hrefs.add("/features.html");
        Set<String> normalized = HtmlUtils.addDomainInRelativeLinks(hrefs, ORIGIN + "index.html");
        Assertions.assertFalse(normalized.isEmpty());
        Assertions.assertEquals(ORIGIN + "features.html", normalized.iterator().next());
    }

    @Test
    void normalizeRelativeLinks() {
        Set<String> hrefs = new HashSet<>();
        // This must stay after filtering
        hrefs.add("../features.html");
        Set<String> normalized = HtmlUtils.addDomainInRelativeLinks(hrefs, ORIGIN + "folder/index.html");
        Assertions.assertFalse(normalized.isEmpty());
        Assertions.assertEquals(ORIGIN + "features.html", normalized.iterator().next());
    }
}
