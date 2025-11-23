package com.web.crawler.controllers;

import com.web.crawler.dto.SearchRequest;
import com.web.crawler.dto.SearchResult;
import com.web.crawler.services.WebSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchApiController {

    private final WebSearchService webSearchService;

    @PostMapping
    public ResponseEntity<List<SearchResult>> search(@RequestBody SearchRequest request) {
        List<SearchResult> results = webSearchService.search(request.getQuery());
        return ResponseEntity.ok(results);
    }
}