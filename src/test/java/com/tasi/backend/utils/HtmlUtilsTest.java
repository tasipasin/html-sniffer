
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
        Assertions.assertTrue(!hrefs.isEmpty());
    }

    @Test
    void filterDomain() {
        Set<String> hrefs = new HashSet<>();
        // This must be keeped
        hrefs.add("/reg/");
        // This must be keeped
        hrefs.add("/settings");
        // This must be keeped
        hrefs.add("/groups/discover/");
        hrefs.add("https://developers.facebook.com/?ref=pf");
        // This must be keeped
        hrefs.add("/help/?ref=pf");
        // This must be keeped
        hrefs.add("/login/");
        hrefs.add("https://messenger.com/");
        hrefs.add("https://www.facebook.com/watch/");
        hrefs.add("https://www.facebook.com/winrar/");
        // This must be keeped
        hrefs.add("/cookies.html");
        // This must be keeped
        hrefs.add("/industry.html");
        hrefs.add("https://winrar.informer.com/");
        // This must be keeped
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
        // This must be keeped
        hrefs.add("/policies/cookies/");
        hrefs.add("https://pay.facebook.com/");
        hrefs.add(
                "https://l.facebook.com/l.php?u=https%3A%2F%2Fwww.instagram.com%2F&h=AT3nS-Sc_s8fq0vUa174yiokS9kiw46kP3_g2jvSn0Tv95zNO1QAmMjgj1c3zGwrjQeLaO1lA6DX9sEl08i4XLRdFdbmzzY8FeYJHT_8UMF1cyzgfpM-JiACn0BOutFwdwauRZrGYrKXFpZo");
        hrefs.add("help/637205020878504");
        // This must be keeped
        hrefs.add("/fundraisers/");
        hrefs.add("https://ar-ar.facebook.com/");
        // This must be keeped
        hrefs.add("/ad_campaign/landing.php?placement=pflo&campaign_id=402047449186&nav_source=unknown&extra_1=auto");
        // This must be keeped
        hrefs.add("/pages/create/?ref_type=site_footer");
        hrefs.add("https://about.meta.com/");
        hrefs.add("https://it-it.facebook.com/");
        hrefs.add("https://hi-in.facebook.com/");
        HtmlUtils.filterSameDomainLinks(hrefs, ORIGIN);
        Assertions.assertFalse(hrefs.isEmpty());
        Assertions.assertEquals(21, hrefs.size());
    }

    @Test
    void normalizeLinks() {
        Set<String> hrefs = new HashSet<>();
        // This must stay after filtering
        hrefs.add("/reg/");
        Set<String> normalized = HtmlUtils.addDomainInRelativeLinks(hrefs, ORIGIN);
        Assertions.assertFalse(normalized.isEmpty());
        Assertions.assertEquals(ORIGIN + "/reg/", normalized.iterator().next());
    }
}
