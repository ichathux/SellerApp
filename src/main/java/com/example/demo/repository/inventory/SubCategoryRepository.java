package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategory, String> {

    Optional<Iterable<SubCategory>> findAllByCategory(Category category);

    Optional<SubCategory> findSubCategoryByName(String name);
}
