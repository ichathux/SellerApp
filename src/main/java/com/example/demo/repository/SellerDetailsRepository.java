package com.example.demo.repository;

import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerDetailsRepository extends CrudRepository<SellerDetails, Long> {

    Optional<SellerDetails> findByUser(User user);
}
