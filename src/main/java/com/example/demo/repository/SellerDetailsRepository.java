package com.example.demo.repository;

import com.example.demo.model.SellerDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerDetailsRepository extends MongoRepository<SellerDetails, String> {

    Optional<SellerDetails> findByUsername(String username);
    void deleteByUsername(String username);
}
