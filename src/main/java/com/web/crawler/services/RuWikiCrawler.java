package com.web.crawler.services;
import org.springframework.stereotype.Service;

@Service("ruWikiCrawler")
public class RuWikiCrawler extends AbstractMediaWikiCrawler {

    @Override
    protected String getBaseUrl() {
        return "https://ru.ruwiki.ru/wiki/";
    }

    @Override
    protected String[] getIgnoredPrefixes() {
        return new String[]{
                "Служебная:", "Файл:", "Категория:", "Обсуждение:",
                "Участник:", "Шаблон:", "Справка:", "Портал:",
                "Special:", "File:", "Category:"
        };
    }
}