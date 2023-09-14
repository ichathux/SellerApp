package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    @Query("{name:'?0'}")
    Optional<Brand> findByName(String name);
}