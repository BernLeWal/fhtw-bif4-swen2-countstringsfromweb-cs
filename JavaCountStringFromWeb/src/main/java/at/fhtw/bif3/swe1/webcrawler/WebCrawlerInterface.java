package at.fhtw.bif3.swe1.webcrawler;

import java.util.List;

public interface WebCrawlerInterface {
    String getWebsiteDomain();

    String getContentString();

    List<String> getHeadlines(String pattern, int group);
}
