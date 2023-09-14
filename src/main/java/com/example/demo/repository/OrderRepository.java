package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Orders, String> {
    Optional<Page<Orders>> findAllBySellerUsernameOrderByIdDesc(String sellerUsername,
                                                                Pageable pageable);
    Optional<Page<Orders>> findBySellerUsernameAndCreatedAtBetweenOrderByIdDesc(String sellerUsername,
                                                                                Instant startDate,
                                                                                Instant endDate,
                                                                                Pageable pageable);
    Optional<Page<Orders>> findByIdAndSellerUsernameOrderByIdDesc(Long id,
                                                                  String sellerUsername,
                                                                  Pageable pageable);

    Optional<Page<Orders>> findAllByCustomerAndSellerUsernameOrderByIdDesc(Customer customer ,
                                                                           String sellerUsername,
                                                                           Pageable pageRequest);
}
