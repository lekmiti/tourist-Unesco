package com.example.touristmicroservice.services;

import com.example.touristmicroservice.models.es.EsSite;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class SiteService {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Autowired
    private Gson gson;


    @GetMapping
    public List<EsSite> getSites(String country) throws IOException {
         SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .size(5000)
                .query(QueryBuilders.matchQuery("states", country));
        return executeQuery(searchSourceBuilder);
    }

    @GetMapping
    public List<EsSite> getSites() throws IOException {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .size(5000)
                .query(QueryBuilders.matchAllQuery());

        return executeQuery(searchSourceBuilder);
    }

    private List<EsSite> executeQuery(SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest("sites");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return Stream.of(searchResponse.getHits().getHits())
                .map(hit -> hit.getSourceAsString())
                .map(site -> gson.fromJson(site, EsSite.class))
                .collect(toList());
    }



}
