package com.example.demo.service;

import com.example.demo.dto.InventoryDto;
import com.example.demo.dto.InventoryRequestDto;
import com.example.demo.dto.InventoryResponseDto;
import com.example.demo.dto.UpdateInventoryDto;
import com.example.demo.model.inventory.Brand;
import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.Inventory;
import com.example.demo.model.inventory.SubCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface InventoryService {
//    ResponseEntity<String> addSingleInventoryItemToDB(InventoryDto inventoryDto);
    ResponseEntity<Iterable<Category>> getCategories();
    ResponseEntity<Iterable<SubCategory>> getSubCategories(Long category);
    ResponseEntity<Iterable<Brand>> getBrands();
    ResponseEntity<Page<InventoryResponseDto>> getInventoryItemsResponse(int page , int size);
    ResponseEntity<String> deleteInventoryItem(Long id);
    ResponseEntity<String> updateInventoryItem(UpdateInventoryDto updateInventoryDto);

    ResponseEntity<String> addInventory(InventoryRequestDto inventoryRequestDto) throws JsonProcessingException;
}