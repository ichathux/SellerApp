package com.example.demo.repository;

import com.example.demo.model.Orders;
import com.example.demo.model.SellerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Orders, Long> {
    Optional<Page<Orders>> findAllBySellerDetailsOrderByIdDesc(SellerDetails sellerDetails, Pageable pageable);
}
