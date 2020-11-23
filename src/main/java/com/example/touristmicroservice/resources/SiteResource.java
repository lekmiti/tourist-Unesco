package com.example.touristmicroservice.resources;


import com.example.touristmicroservice.models.es.EsSite;
import com.example.touristmicroservice.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/sites")
public class SiteResource {

    @Autowired
    private SiteService siteService;

    @GetMapping(params = {"country"})
    public ResponseEntity<List<EsSite>> getSiteByCountry(@RequestParam("country") String country) throws IOException {
        return ResponseEntity.ok(siteService.getSites(country));
    }

    @GetMapping
    public ResponseEntity<List<EsSite>> getSite() throws IOException {
        return ResponseEntity.ok(siteService.getSites());
    }


}
