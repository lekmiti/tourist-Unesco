package com.example.touristmicroservice.repositories.db;

        import com.example.touristmicroservice.models.Tourist;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface TouristRepository extends CrudRepository<Tourist, Long> {
}
