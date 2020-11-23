package com.example.touristmicroservice.resources;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TouristSitesDto {



    private String firstName;

    private String lastName;

    private int passportID;

    private List<SiteDto> sites;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class SiteDto {
        private String name;
        private String imageUrl;
        private String  states;
        private String  descriptionMarkup;
    }
}
