package com.example.touristmicroservice.services;

import com.example.touristmicroservice.models.Site;
import com.example.touristmicroservice.models.Tourist;
import com.example.touristmicroservice.repositories.db.TouristRepository;
import com.example.touristmicroservice.resources.TouristSitesDto;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;

@Service
public class TouristService {

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private Gson gson;


    public Tourist saveNewTourist(Tourist tourist) {
        return touristRepository.save(tourist);
    }

    public Optional<TouristSitesDto> getSitesForTourist(Long id) {
        return touristRepository.findById(id)
                .map(tourist -> {

                    List<TouristSitesDto.SiteDto> siteDtos = getSiteDtosFromElasticsearch(tourist);
                    TouristSitesDto touristSitesDto = buildTouristSite(tourist, siteDtos);

                    return Optional.of(touristSitesDto);})
                .orElse(empty());

    }

    private TouristSitesDto buildTouristSite(Tourist tourist, List<TouristSitesDto.SiteDto> siteDtos) {
        return TouristSitesDto.builder()
                                .firstName(tourist.getFirstName())
                                .lastName(tourist.getLastName())
                                .passportID(tourist.getPassportID())
                                .sites(siteDtos)
                                .build();
    }

    private List<TouristSitesDto.SiteDto> getSiteDtosFromElasticsearch(Tourist tourist) {
        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termsQuery("name.keyword", tourist.getVisitedSites().stream().map(Site::getName).collect(toList())));
        sourceBuilder.size(1000);
        searchRequest.source(sourceBuilder);


//        SearchTemplateRequest request = new SearchTemplateRequest();
//        request.setRequest(new SearchRequest("sites"));
//        request.setScriptType(ScriptType.INLINE);
//        request.setScript(
//                "  " +
//                        "\"query\": {\n" +
//                        "    \"terms\": {\n" +
//                        "      \"name\": [\"<i>Aflaj</i> Irrigation Systems of Oman\", \"<I>Sacri Monti</I> of Piedmont and Lombardy\"],\n" +
//                        "      \"boost\": 1.0\n" +
//                        "    }\n" +
//                        "  }\n" +
//                        "}" +
//                        "");
//        System.out.println(tourist.getVisitedSites().stream().map(Site::getName).collect(toList()).toString());
//        Map<String, Object> scriptParams = new HashMap<>();
////        scriptParams.put("value", tourist.getVisitedSites().stream().map(Site::getName).collect(toList()).toString());
//        scriptParams.put("size", 1000);
//        request.setScriptParams(scriptParams);

        List<TouristSitesDto.SiteDto> siteDtos = new ArrayList<>();
        try {
            siteDtos = Stream.of(restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits())
                    .map(hit -> hit.getSourceAsString())
                    .map(site -> gson.fromJson(site, TouristSitesDto.SiteDto.class))
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return siteDtos;
    }
}
