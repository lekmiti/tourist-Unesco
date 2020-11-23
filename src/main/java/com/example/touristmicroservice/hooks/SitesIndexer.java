package com.example.touristmicroservice.hooks;

import com.example.touristmicroservice.models.es.EsSite;
import com.example.touristmicroservice.remote.SiteClient;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static java.time.LocalDate.now;
import static org.elasticsearch.common.xcontent.XContentType.JSON;

@Component
@Slf4j
public class SitesIndexer {


    private static final Logger LOGGER = LoggerFactory.getLogger(SitesIndexer.class);

    private static final String SITES_INDEX = "sites";
     private static final String SITES_INDEX_ALIAS = "sites";

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private SiteClient siteClient;


    @Autowired
    private Gson gson;


    @Scheduled(fixedDelay = 86400000, initialDelay = 5000)
    public void reindex() throws IOException {
        LOGGER.info("start reindexing again...");
        List<EsSite> uniscoSites = siteClient.getSites();


        // delete index if it exists
        boolean indexExist = restHighLevelClient.indices().exists(new GetIndexRequest(SITES_INDEX), RequestOptions.DEFAULT);

        if(indexExist) {
            DeleteIndexRequest deleteIndex = new DeleteIndexRequest(SITES_INDEX);
            restHighLevelClient.indices().delete(deleteIndex, RequestOptions.DEFAULT);
        }

        BulkRequest bulkRequest = new BulkRequest();
        uniscoSites.forEach(site -> {
            IndexRequest indexRequest = new IndexRequest(SITES_INDEX)
                    .source(gson.toJson(site), JSON);

            bulkRequest.add(indexRequest);
        });
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        LOGGER.info("end reindexing...");

    }


/*
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void reindex() throws IOException {
        List<EsSite> uniscoSites = siteClient.getSites();

        GetAliasesRequest requestWithAlias = new GetAliasesRequest(SITES_INDEX_ALIAS);
        boolean exists = restHighLevelClient.indices().existsAlias(requestWithAlias, RequestOptions.DEFAULT);
        GetAliasesResponse response = restHighLevelClient.indices().getAlias(requestWithAlias, RequestOptions.DEFAULT);

        String indexName = SITES_INDEX + now();
        if(exists) {

            bulkDataToIndex(uniscoSites, indexName);
            IndicesAliasesRequest request = new IndicesAliasesRequest();
            AliasActions aliasAction = new AliasActions(ADD)
                                            .index(indexName)
                                            .alias(SITES_INDEX_ALIAS);
            request.addAliasAction(aliasAction);

            AliasActions removeAction =
                    new AliasActions(AliasActions.Type.REMOVE)
                            .index(SITES_INDEX)
                            .alias(SITES_INDEX_ALIAS);

            request.addAliasAction(aliasAction);

            restHighLevelClient.indices().updateAliases(request, RequestOptions.DEFAULT);

        } else {

            bulkDataToIndex(uniscoSites, indexName);
            mapAliasToIndex(indexName);

        }

    }*/
  /*  private void bulkDataToIndex(List<EsSite> uniscoSites, String indexName) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();

        uniscoSites.forEach(site -> {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            IndexRequest indexRequest = new IndexRequest(indexName)
                    .source(gson.toJson(site), JSON);

            bulkRequest.add(indexRequest);
        });
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    }

    private void mapAliasToIndex(String indexName) throws IOException {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        AliasActions aliasAction = new AliasActions(ADD)
                .index(indexName)
                .alias(SITES_INDEX_ALIAS);
        request.addAliasAction(aliasAction);
        restHighLevelClient.indices().updateAliases(request, RequestOptions.DEFAULT);
    }*/

/*
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void reindex()   {
        LOGGER.info("executed again");

        List<EsSite> sites = siteClient.getSites();

        LOGGER.info("start indexing...");

        BulkRequest bulkRequest = new BulkRequest();


        sites.forEach(site -> {
            IndexRequest  indexRequest = new IndexRequest(SITES_INDEX)
                    .source(gson.toJson(site),JSON);

            bulkRequest.add(indexRequest);
        });

        try {
          restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            LOGGER.info("end indexing");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("error msg:" + e);
         }
    }*/
}
