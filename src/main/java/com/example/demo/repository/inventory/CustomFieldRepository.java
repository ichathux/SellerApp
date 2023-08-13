package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.CustomFieldData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomFieldRepository extends CrudRepository<CustomFieldData, String> {

    Optional<CustomFieldData> findByUsernameAndVariantType(String username, String type);
    Optional<Iterable<CustomFieldData>> findByUsername(String username);
}
