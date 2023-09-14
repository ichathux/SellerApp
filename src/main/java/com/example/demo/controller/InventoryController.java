package com.example.demo.controller;

import com.example.demo.dto.InventoryRequestDto;
import com.example.demo.dto.InventoryResponseDto;
import com.example.demo.dto.UpdateInventoryDto;
import com.example.demo.model.inventory.Brand;
import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.SubCategory;
import com.example.demo.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * manage inventory related flow
 */
@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
@Slf4j
public class InventoryController {


    private final InventoryService inventoryService;
    /**
     * add new new inventory item to DB
     * @param inventoryRequestDto
     * @return ResponseEntity<OK> or ResponseEntity<Error>
     * @throws JsonProcessingException db row containing json format string and after retrieve it
     *                                 convert as a array.
     */
    @PostMapping("add")
    private ResponseEntity<String> add(@RequestBody InventoryRequestDto inventoryRequestDto)
            throws JsonProcessingException {
        return inventoryService.addInventory(inventoryRequestDto);
    }

    /**
     * get all Inventory list that contain on DB
     *
     * @param page
     * @param size
     * @return ResponseEntity<Page < InventoryResponseDto>>
     */
    @GetMapping("get")
    private ResponseEntity<Page<InventoryResponseDto>> getAllItems(
            @RequestParam("page") int page ,
            @RequestParam("size") int size) {
//        log.info("getting inventory");
        return inventoryService.getInventoryItemsResponse(page , size);
    }

    /**
     * delete given item from DB by ID
     *
     * @param id
     * @return ResponseEntity<' Done '> or ResponseEntity<'error'>
     */
    @DeleteMapping("delete/{id}")
    private ResponseEntity<String> delete(@PathVariable String id) {
        System.out.println("deleting "+id);
        return inventoryService.deleteInventoryItem(id);
    }


    @PutMapping("update")
    private ResponseEntity<String> update(@RequestBody InventoryResponseDto dto) {
        return inventoryService.updateInventoryItem(dto);
    }


    /**
     * get all Category list that contain on DB
     *
     * @return ResponseEntity<Iterable < Category>>
     */
    @GetMapping("getCategories")
    private ResponseEntity<Iterable<Category>> getCategories() {
        return inventoryService.getCategories();
    }

    /**
     * get all SubCategory list that contain on DB
     *
     * @param - categoryId
     * @return - ResponseEntity<Iterable<SubCategory>>
     */
    @GetMapping("getSubCategories")
    private ResponseEntity<Iterable<SubCategory>> getSubCategories(
            @RequestParam("id") String category) {
        return inventoryService.getSubCategories(category);
    }

    /**
     * get all brand list that contain on DB
     *
     * @return ResponseEntity<Iterable < Brand>>
     */
    @GetMapping("getBrands")
    private ResponseEntity<Iterable<Brand>> getAllBrand() {
        return inventoryService.getBrands();
    }

}
