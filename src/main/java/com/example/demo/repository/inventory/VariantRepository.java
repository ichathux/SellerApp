package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {
}
