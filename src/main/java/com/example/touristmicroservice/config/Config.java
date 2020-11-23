package com.example.touristmicroservice.config;


import com.example.touristmicroservice.remote.SiteClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableFeignClients(basePackageClasses = {SiteClient.class})
@EnableElasticsearchRepositories(basePackages = "com.example.touristmicroservice.repositories.es")
@EnableScheduling
public class Config {
}
