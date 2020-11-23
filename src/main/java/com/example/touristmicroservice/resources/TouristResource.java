package com.example.touristmicroservice.resources;

import com.example.touristmicroservice.models.Tourist;
import com.example.touristmicroservice.services.TouristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/api/tourists")
public class TouristResource {

    @Autowired
    private TouristService touristService;

    @GetMapping("/{id}")
    public ResponseEntity<TouristSitesDto> getTouristData(@PathVariable("id") Long id)  {
        Optional<TouristSitesDto> maybeSitesForTourist = touristService.getSitesForTourist(id);
       return  maybeSitesForTourist.isPresent() ?
                ok(maybeSitesForTourist.get()) : notFound().build();
     }
     @PostMapping
    public ResponseEntity<Tourist> create(@RequestBody Tourist tourist) {

         return ResponseEntity.created(URI.create("/v1/api/tourists")).body(touristService.saveNewTourist(tourist));
     }

}
