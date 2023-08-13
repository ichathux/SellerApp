package com.example.demo.controller;

import com.example.demo.dto.InventoryDto;
import com.example.demo.model.ListingFileUpload;
import com.example.demo.model.inventory.Brand;
import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.Inventory;
import com.example.demo.model.inventory.SubCategory;
import com.example.demo.service.AccountService;
import com.example.demo.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    private final AccountService accountService;

    @PostMapping(value = "addSingleItem", consumes = MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<String> addSingleItems(@RequestPart("file") MultipartFile file ,
                                                  @RequestParam("name") String name ,
                                                  @RequestParam("subCategoryId") Long subCategoryId ,
                                                  @RequestParam("brand") String brand ,
//                                                  @RequestParam("quantity") Integer quantity ,
//                                                  @RequestParam("unitPrice") double unitPrice ,
                                                  @RequestParam("itemDescription") String itemDescription,
                                                  @RequestParam("customField1") Long customField1,
                                                  @RequestParam("customField1Price") Double customField1Price,
                                                  @RequestParam("customField2") Long customField2,
                                                  @RequestParam("customField2Price") Double customField2Price,
                                                  @RequestParam("customField3") Long customField3,
                                                  @RequestParam("customField3Price") Double customField3Price,
                                                  @RequestParam("customField4") Long customField4,
                                                  @RequestParam("customField4Price") Double customField4Price,
                                                  @RequestParam("customField5") Long customField5,
                                                  @RequestParam("customField5Price") Double customField5Price,
                                                  @RequestParam("customField6") Long customField6,
                                                  @RequestParam("customField6Price") Double customField6Price,
                                                  @RequestParam("variantType") String variantType) {

        return inventoryService.addSingleItemToInventory(
                new InventoryDto(name ,
                        subCategoryId ,
                        brand ,
                        itemDescription ,
                        file,
                        Long.valueOf(customField1),
                        Double.valueOf(customField1Price),
                        Long.valueOf(customField2),
                        Double.valueOf(customField2Price),
                        Long.valueOf(customField3),
                        Double.valueOf(customField3Price),
                        Long.valueOf(customField4),
                        Double.valueOf(customField4Price),
                        Long.valueOf(customField5),
                        Double.valueOf(customField5Price),
                        Long.valueOf(customField6),
                        Double.valueOf(customField6Price),
                        variantType));
    }

    @GetMapping("getAllItems")
    private ResponseEntity<Page<Inventory>> getAllItems(
            @RequestParam("page") int page ,
            @RequestParam("size") int size) {
        return inventoryService.getAllItems(page,size);
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
        return inventoryService.getAllBrands();
    }


}
