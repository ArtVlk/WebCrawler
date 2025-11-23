package com.web.crawler.services;
import org.springframework.stereotype.Service;

@Service("englishWikiCrawler")
public class EnglishWikipediaCrawler extends AbstractMediaWikiCrawler {

    @Override
    protected String getBaseUrl() {
        return "https://en.wikipedia.org/wiki/";
    }

    @Override
    protected String[] getIgnoredPrefixes() {
        return new String[]{
                "Help:", "File:", "Category:", "Special:",
                "Talk:", "Portal:", "Template:", "User:"
        };
    }
}