package com.web.crawler.services;

import com.web.crawler.dto.SearchResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WikipediaSearchService implements WebSearchService{
    private static final String BASE_URL = "https://en.wikipedia.org/w/api.php";

    @Override
    public List<SearchResult> search(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String fullUrl = BASE_URL + "?action=query&list=search&srsearch=" + encodedQuery + "&utf8=&format=json";

            URI uri = URI.create(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.lines().collect(Collectors.joining());
            in.close();

            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONObject("query").getJSONArray("search");

            List<SearchResult> searchResults = new ArrayList<>();
            for (int i = 0; i < Math.min(10, results.length()); i++) {
                JSONObject result = results.getJSONObject(i);
                String title = result.getString("title");
                String snippet = result.getString("snippet").replaceAll("(<[^>]+>)", "");
                String pageUrl = "https://en.wikipedia.org/wiki/" + URLEncoder.encode(title.replace(" ", "_"), StandardCharsets.UTF_8);
                searchResults.add(new SearchResult(pageUrl, title, snippet));
            }
            System.out.println("Поиск: " + query);
            return searchResults;
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Ошибка при поиске статей", e);
        }
    }

}
