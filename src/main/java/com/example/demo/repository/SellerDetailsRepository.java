package com.example.demo.repository;

import com.example.demo.model.SellerDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerDetailsRepository extends CrudRepository<SellerDetails, Long> {

    Optional<SellerDetails> findByUsername(String username);
//    Optional<SellerDetails> findByUsername(String username);
}
