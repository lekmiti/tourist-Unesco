package com.example.touristmicroservice.models.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "site")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class EsSite {

    @Id
    private Long id;

    private String name;

    private int yearInscribed;

    private String url;

    private String imageUrl;

    private String descriptionMarkup;

    private String states;

    private String categoryName;

    private String regionName;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Location location;


    @Data @AllArgsConstructor @NoArgsConstructor
    public class Location {

        private String name;

        private int longitude;

        private int latitude;
    }

}
