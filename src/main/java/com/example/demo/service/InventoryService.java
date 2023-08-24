package com.example.demo.service;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.CustomFieldResponseDto;
import com.example.demo.dto.InventoryDto;
import com.example.demo.dto.InventoryResponseDto;
import com.example.demo.dto.UpdateInventoryDto;
import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.inventory.*;
import com.example.demo.repository.inventory.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryService {
    private final CustomFieldRepository customFieldRepository;
    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final UserAuthProvider userAuthProvider;
    private final CloudinaryService cloudinaryService;

    public ResponseEntity<String> addSingleItemToInventory(InventoryDto inventoryDto) {
        String username = userAuthProvider.getCurrentUserUsername();
        log.info(username + ", addSingleItemToInventory, " + inventoryDto);
        Inventory inventory = new Inventory();
        Optional<Brand> brand = brandRepository.findByName(inventoryDto.getBrand());
        if (brand.isPresent()) {
            inventory.setBrand(brand.get());
        } else {
            Brand brand1 = new Brand();
            brand1.setName(inventoryDto.getBrand());
            inventory.setBrand(brandRepository.save(brand1));
        }

        inventory.setName(inventoryDto.getName());
        inventory.setSubCategory(subCategoryRepository
                .findById(inventoryDto.getSubCategoryId())
                .orElseThrow(() -> new SpringException("subcategory not found")));
        inventory.setItemDescription(inventoryDto.getItemDescription());
        inventory.setSellerUsername(username);
        inventory.setCreatedAt(Instant.now());
        inventory.setEnabled(true);
        inventory.setStatus(Status.IN_STOCK);
        inventory.setCustomField1(inventoryDto.getCustomField1());
        inventory.setCustomField1Price(inventoryDto.getCustomField1Price());
        inventory.setCustomField2(inventoryDto.getCustomField2());
        inventory.setCustomField2Price(inventoryDto.getCustomField2Price());
        inventory.setCustomField3(inventoryDto.getCustomField3());
        inventory.setCustomField3Price(inventoryDto.getCustomField3Price());
        inventory.setCustomField4(inventoryDto.getCustomField4());
        inventory.setCustomField4Price(inventoryDto.getCustomField4Price());
        inventory.setCustomField5(inventoryDto.getCustomField5());
        inventory.setCustomField5Price(inventoryDto.getCustomField5Price());
        inventory.setCustomField6(inventoryDto.getCustomField6());
        inventory.setCustomField6Price(inventoryDto.getCustomField6Price());
        inventory.setCustomFieldData(customFieldRepository
                .findByUsernameAndVariantType(username , inventoryDto.getVariantType().trim())
                .orElseThrow(() -> new SpringException("Custom field not found")));
        inventory.setDltUrl(inventoryDto.getDltUrl());
        inventory.setImgUrl(inventoryDto.getImgUrl());
        inventoryRepository.save(inventory);
        return new ResponseEntity<>("done" , HttpStatus.OK);

    }

    public ResponseEntity<Iterable<Category>> getCategories() {
        return new ResponseEntity<>(categoryRepository.findAll() , HttpStatus.OK);
    }

    public ResponseEntity<Iterable<SubCategory>> getSubCategories(Long category) {
        Optional<Iterable<SubCategory>> categories = subCategoryRepository.findAllByCategory(category);

        return categories
                .map(subCategories -> new ResponseEntity<>(subCategories , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }


    public ResponseEntity<Iterable<Brand>> getAllBrands() {
        return new ResponseEntity<>(brandRepository.findAll() , HttpStatus.OK);
    }

    public ResponseEntity<Page<InventoryResponseDto>> getAllItems(int page , int size) {
        return mappingCustomFieldsToVariants(page , size);
    }

    public ResponseEntity<Page<InventoryResponseDto>> mappingCustomFieldsToVariants(int page , int size) {
        Optional<Page<Inventory>> itemInventory = inventoryRepository
                .findAllBySellerUsernameOrderByIdDesc(
                        userAuthProvider.getCurrentUserUsername() ,
                        PageRequest
                                .of(page , size));
        List<InventoryResponseDto> returnResponseList = new ArrayList<>();

        if (itemInventory.isPresent()) {
            List<Inventory> list = itemInventory.get().getContent();

            for (Inventory inventory : list) {
                CustomFieldData customFieldData = inventory.getCustomFieldData();
                int customFieldsSize = customFieldData.getFieldsUsed();
                List<CustomFieldResponseDto> customFieldResponseDtos = new ArrayList<>();
                var inventoryResponse = new InventoryResponseDto();
                for (int i = 0; i < customFieldsSize; i++) {
                    var customFieldResponseDto = new CustomFieldResponseDto();

                    switch (i) {
                        case 0:
                            customFieldResponseDto = new CustomFieldResponseDto(
                                    customFieldData.getCustomField1() ,
                                    inventory.getCustomField1() ,
                                    inventory.getCustomField1Price());

                            break;
                        case 1:
                            customFieldResponseDto = new CustomFieldResponseDto(
                                    customFieldData.getCustomField2() ,
                                    inventory.getCustomField2() ,
                                    inventory.getCustomField2Price());
                            break;
                        case 2:
                            customFieldResponseDto = new CustomFieldResponseDto(
                                    customFieldData.getCustomField3() ,
                                    inventory.getCustomField3() ,
                                    inventory.getCustomField3Price());
                            break;
                        case 3:
                            customFieldResponseDto = new CustomFieldResponseDto(
                                    customFieldData.getCustomField4() ,
                                    inventory.getCustomField4() ,
                                    inventory.getCustomField4Price());
                            break;
                        case 4:
                            customFieldResponseDto = new CustomFieldResponseDto(
                                    customFieldData.getCustomField5() ,
                                    inventory.getCustomField5() ,
                                    inventory.getCustomField5Price());
                            break;
                        case 5:
                            customFieldResponseDto = new CustomFieldResponseDto(
                                    customFieldData.getCustomField6() ,
                                    inventory.getCustomField6() ,
                                    inventory.getCustomField6Price());
                            break;

                    }
                    if (customFieldResponseDto.getQty() > 0) {
                        customFieldResponseDtos.add(customFieldResponseDto);
                    }
                }

                inventoryResponse.setId(inventory.getId());
                inventoryResponse.setVariants(customFieldResponseDtos);
                inventoryResponse.setName(inventory.getName());
                inventoryResponse.setBrand(inventory.getBrand().getName());
                inventoryResponse.setSubCategoryId(inventory.getSubCategory());
                inventoryResponse.setCreatedAt(inventory.getCreatedAt());
                inventoryResponse.setImage(inventory.getImgUrl());
                inventoryResponse.setItemDescription(inventory.getItemDescription());
                inventoryResponse.setPublicId(inventory.getDltUrl());
                returnResponseList.add(inventoryResponse);
            }

            int totalPages = itemInventory.get().getTotalPages() * itemInventory.get().getSize();
            return new ResponseEntity<>(new PageImpl<>(
                    returnResponseList ,
                    itemInventory.get().getPageable() ,
                    totalPages) ,
                    HttpStatus.OK);

        }
        return new ResponseEntity<>(null , HttpStatus.NO_CONTENT);

    }


    public ResponseEntity<String> deleteInventoryItem(Long id) {
        log.info(userAuthProvider.getCurrentUserUsername() + ", delete inventory item, " + id);
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()) {
                if (inventory.get().getDltUrl() != null)
                    cloudinaryService.removeImg(inventory.get().getDltUrl());
                inventoryRepository.deleteById(id);
            }

            return new ResponseEntity<>("done" , HttpStatus.OK);
        } catch (Exception e) {
            log.error(userAuthProvider.getCurrentUserUsername() + ", error occurred while deleting inventory, " + id);
            return new ResponseEntity<>("error" , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> updateInventoryItem(UpdateInventoryDto updateInventoryDto){
        Optional<Inventory> inventory = inventoryRepository.findById(updateInventoryDto.getId());
        if (inventory.isPresent()){
            Inventory inventory1 = inventory.get();
            if (updateInventoryDto.isImageUpdated()){
                cloudinaryService.removeImg(inventory1.getDltUrl());
            }
            inventory1.setName(updateInventoryDto.getName());
            inventory1.setImgUrl(updateInventoryDto.getImgUrl());
            inventory1.setDltUrl(updateInventoryDto.getPublicID());
            inventory1.setItemDescription(updateInventoryDto.getItemDescription());
            inventory1.setCustomField1(updateInventoryDto.getCustomField1());
            inventory1.setCustomField1Price(updateInventoryDto.getCustomField1Price());
            inventory1.setCustomField2(updateInventoryDto.getCustomField2());
            inventory1.setCustomField2Price(updateInventoryDto.getCustomField2Price());
            inventory1.setCustomField3(updateInventoryDto.getCustomField3());
            inventory1.setCustomField3Price(updateInventoryDto.getCustomField3Price());
            inventory1.setCustomField4(updateInventoryDto.getCustomField4());
            inventory1.setCustomField4Price(updateInventoryDto.getCustomField4Price());
            inventory1.setCustomField5(updateInventoryDto.getCustomField5());
            inventory1.setCustomField5Price(updateInventoryDto.getCustomField5Price());
            inventory1.setCustomField6(updateInventoryDto.getCustomField6());
            inventory1.setCustomField6Price(updateInventoryDto.getCustomField6Price());
            inventoryRepository.save(inventory1);

            return new ResponseEntity<>("Done", HttpStatus.OK);
        }
        return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
    }
}
