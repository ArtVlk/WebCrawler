package com.web.crawler.services;

import com.web.crawler.dto.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractMediaWikiCrawler implements WebSearchService {

    private static final int MAX_RESULTS = 10;
    private static final int MAX_DEPTH = 2;
    private static final int TIMEOUT = 10000;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
            " (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    protected abstract String getBaseUrl();
    protected abstract String[] getIgnoredPrefixes();

    @Override
    public List<SearchResult> search(String query) {
        String encodedQuery = URLEncoder.encode(query.trim().replace(" ", "_"), StandardCharsets.UTF_8);
        String startUrl = getBaseUrl() + encodedQuery;

        try {
            return bfsCrawl(startUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<SearchResult> bfsCrawl(String startUrl) {
        Queue<CrawlNode> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        List<SearchResult> results = new ArrayList<>();

        queue.add(new CrawlNode(startUrl, 0));
        visited.add(startUrl);

        while (!queue.isEmpty() && results.size() < MAX_RESULTS) {
            CrawlNode node = queue.poll();
            if (node.depth > MAX_DEPTH) continue;

            Document doc;
            try {
                doc = Jsoup.connect(node.url)
                        .userAgent(USER_AGENT)
                        .timeout(TIMEOUT)
                        .followRedirects(true)
                        .get();
            } catch (IOException e) {
                System.err.println("Error connecting to: " + node.url);
                continue;
            }

            SearchResult info = extractArticleInfo(doc, node.url);
            if (info != null) {
                results.add(info);
                if (results.size() >= MAX_RESULTS) break;
            }

            List<String> links = extractLinks(doc);
            for (String link : links) {
                if (!visited.contains(link)) {
                    visited.add(link);
                    queue.add(new CrawlNode(link, node.depth + 1));
                }
            }
        }
        return results;
    }

    private SearchResult extractArticleInfo(Document doc, String url) {
        try {
            String title = doc.select("#firstHeading").text();
            if (title.isBlank()) return null;

            Element p = doc.select("#mw-content-text p").stream()
                    .filter(el -> el.text().trim().length() > 40)
                    .findFirst().orElse(null);

            String snippet = p != null ? p.text() : "Описание отсутствует.";
            return new SearchResult(url, title, snippet);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> extractLinks(Document doc) {
        return doc.select("#mw-content-text a[href]").stream()
                .map(el -> el.absUrl("href"))
                .filter(href -> href.startsWith(getBaseUrl()))
                .filter(href -> !isServicePage(href))
                .distinct()
                .limit(50)
                .collect(Collectors.toList());
    }

    private boolean isServicePage(String href) {
        for (String prefix : getIgnoredPrefixes()) {
            if (href.contains(prefix)) return true;
        }
        return false;
    }

    private static class CrawlNode {
        String url;
        int depth;
        CrawlNode(String url, int depth) { this.url = url; this.depth = depth; }
    }
}