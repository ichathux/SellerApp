package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.SubCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {

    Optional<Iterable<SubCategory>> findAllByCategory(Category category);
}
