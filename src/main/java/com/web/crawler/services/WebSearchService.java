package com.web.crawler.services;

import com.web.crawler.dto.SearchResult;

import java.util.List;

public interface WebSearchService {
    List<SearchResult> search(String query);
}
