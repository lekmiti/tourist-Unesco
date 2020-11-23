package com.example.touristmicroservice;

import com.example.touristmicroservice.models.Site;
import com.example.touristmicroservice.models.Tourist;
import com.example.touristmicroservice.repositories.db.TouristRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
 public class TouristMicroserviceApplication {

	@Autowired
	private TouristRepository touristRepository;

/*
	@Autowired
	private SiteRepository siteRepository;
*/

	public static void main(String[] args) {
		SpringApplication.run(TouristMicroserviceApplication.class, args);

  	}

	@Bean
	public CommandLineRunner demo(TouristRepository repository) {
		return (args) -> {
 			Site site = new Site();
			site.setName("<i>Aflaj</i> Irrigation Systems of Oman");
			Site site2 = new Site();
			site2.setName("<I>Sacri Monti</I> of Piedmont and Lombardy");
			Tourist tourist = new Tourist( 1L, "Mohamed Hadi", "Lekmiti", 31, 132132, "earth citizen", List.of(site,site2));
 			repository.save(tourist);
		};
	}

}
