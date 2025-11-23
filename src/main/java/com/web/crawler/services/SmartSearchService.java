package com.web.crawler.services;

import com.web.crawler.dto.SearchResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Primary
public class SmartSearchService implements WebSearchService {

    private final WebSearchService englishCrawler;
    private final WebSearchService ruWikiCrawler;

    public SmartSearchService(
            @Qualifier("englishWikiCrawler") WebSearchService englishCrawler,
            @Qualifier("ruWikiCrawler") WebSearchService ruWikiCrawler) {
        this.englishCrawler = englishCrawler;
        this.ruWikiCrawler = ruWikiCrawler;
    }

    @Override
    @Cacheable(value = "wiki_results", key = "#query")
    public List<SearchResult> search(String query) {
        if (isCyrillic(query)) {
            System.out.println("Запрос на русском: ищем в RuWiki...");
            return ruWikiCrawler.search(query);
        } else {
            System.out.println("Запрос на английском: ищем в Wikipedia...");
            return englishCrawler.search(query);
        }
    }

    private boolean isCyrillic(String text) {
        return Pattern.compile("[а-яА-ЯёЁ]").matcher(text).find();
    }
}