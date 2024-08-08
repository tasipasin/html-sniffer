
package com.tasi.backend.utils;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HtmlUtilsTest {

    private static final String ORIGIN = "https://www.facebook.com/";

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
        // This must stay after filtering
        hrefs.add("/reg/");
        // This must stay after filtering
        hrefs.add("/settings");
        // This must stay after filtering
        hrefs.add("/groups/discover/");
        hrefs.add("https://developers.facebook.com/?ref=pf");
        // This must stay after filtering
        hrefs.add("/help/?ref=pf");
        // This must stay after filtering
        hrefs.add("/login/");
        hrefs.add("https://messenger.com/");
        // This must stay after filtering
        hrefs.add("https://www.facebook.com/watch/");
        // This must stay after filtering
        hrefs.add("/policies/cookies/");
        hrefs.add("https://pay.facebook.com/");
        hrefs.add(
                "https://l.facebook.com/l.php?u=https%3A%2F%2Fwww.instagram.com%2F&h=AT3nS-Sc_s8fq0vUa174yiokS9kiw46kP3_g2jvSn0Tv95zNO1QAmMjgj1c3zGwrjQeLaO1lA6DX9sEl08i4XLRdFdbmzzY8FeYJHT_8UMF1cyzgfpM-JiACn0BOutFwdwauRZrGYrKXFpZo");
        // This must stay after filtering
        hrefs.add("help/637205020878504");
        // This must stay after filtering
        hrefs.add("/fundraisers/");
        hrefs.add("https://ar-ar.facebook.com/");
        // This must stay after filtering
        hrefs.add("/ad_campaign/landing.php?placement=pflo&campaign_id=402047449186&nav_source=unknown&extra_1=auto");
        // This must stay after filtering
        hrefs.add("/pages/create/?ref_type=site_footer");
        hrefs.add("https://about.meta.com/");
        hrefs.add("https://it-it.facebook.com/");
        hrefs.add("https://hi-in.facebook.com/");
        HtmlUtils.filterSameDomainLinks(hrefs, ORIGIN);
        Assertions.assertFalse(hrefs.isEmpty());
        Assertions.assertEquals(11, hrefs.size());
    }
}
