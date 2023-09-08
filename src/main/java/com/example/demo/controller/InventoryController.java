package com.example.demo.controller;

import com.example.demo.dto.InventoryDto;
import com.example.demo.dto.InventoryRequestDto;
import com.example.demo.dto.InventoryResponseDto;
import com.example.demo.dto.UpdateInventoryDto;
import com.example.demo.model.inventory.Brand;
import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.Inventory;
import com.example.demo.model.inventory.SubCategory;
import com.example.demo.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("addSingleItemTest")
    private ResponseEntity<String> add(@RequestBody InventoryRequestDto inventoryRequestDto) throws JsonProcessingException {

        return inventoryService.addInventory(inventoryRequestDto);
    }
    @GetMapping("getAllItems")
    private ResponseEntity<Page<InventoryResponseDto>> getAllItems(
            @RequestParam("page") int page ,
            @RequestParam("size") int size) {

        log.info("getting inventory");
        return inventoryService.getInventoryItemsResponse(page , size);
    }

    @GetMapping("getCategories")
    private ResponseEntity<Iterable<Category>> getCategories() {
        return inventoryService.getCategories();
    }

    @GetMapping("getSubCategories")
    private ResponseEntity<Iterable<SubCategory>> getSubCategories(
            @RequestParam("id") Long category) {
        System.out.println("getting sub categories");
        return inventoryService.getSubCategories(category);
    }

    @GetMapping("getBrands")
    private ResponseEntity<Iterable<Brand>> getAllBrand() {
        return inventoryService.getBrands();
    }

    @DeleteMapping("deleteInventoryItem")
    private ResponseEntity<String> delete(@RequestParam("itemId") Long id) {
        return inventoryService.deleteInventoryItem(id);
    }

    @PutMapping("update")
    private ResponseEntity<String> update(@RequestParam("id") Long id,
                                          @RequestParam("name") String name ,
                                          @RequestParam("imgUrl") String imgUrl,
                                          @RequestParam("delete_url") String publicId,
                                          @RequestParam("itemDescription") String itemDescription ,
                                          @RequestParam("customField1") Long customField1 ,
                                          @RequestParam("customField1Price") Double customField1Price ,
                                          @RequestParam("customField2") Long customField2 ,
                                          @RequestParam("customField2Price") Double customField2Price ,
                                          @RequestParam("customField3") Long customField3 ,
                                          @RequestParam("customField3Price") Double customField3Price ,
                                          @RequestParam("customField4") Long customField4 ,
                                          @RequestParam("customField4Price") Double customField4Price ,
                                          @RequestParam("customField5") Long customField5 ,
                                          @RequestParam("customField5Price") Double customField5Price ,
                                          @RequestParam("customField6") Long customField6 ,
                                          @RequestParam("customField6Price") Double customField6Price ,
                                          @RequestParam("isImageUpdated") Boolean isImageUpdated ,
                                          @RequestParam("variantType") String variantType ){
        return inventoryService.updateInventoryItem(new UpdateInventoryDto(
                id,
                name,
                itemDescription,
                customField1,
                customField1Price,
                customField2,
                customField2Price,
                customField3,
                customField3Price,
                customField4,
                customField4Price,
                customField5,
                customField5Price,
                customField6,
                customField6Price,
                imgUrl,
                publicId,
                isImageUpdated));
    }
}
