package com.example.demo.repository.inventory;

import com.example.demo.model.inventory.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Optional<Page<Inventory>> findAllBySellerUsernameOrderByIdDesc(String sellerUserUsername , Pageable pageRequest);
}
