package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.CustomFieldData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomFieldRepository extends MongoRepository<CustomFieldData, String> {

    @Query("{username:'?0', type: '?1'}")
    Optional<CustomFieldData> findByUsernameAndVariantType(String username, String type);
    @Query("{username:'?0'}")
    Optional<Iterable<CustomFieldData>> findByUsername(String username);
}
