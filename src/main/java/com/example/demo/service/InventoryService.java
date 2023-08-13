package com.example.demo.service;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.InventoryDto;
import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.inventory.*;
import com.example.demo.repository.inventory.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ResponseEntity<String> addSingleItemToInventory(InventoryDto inventoryDto) {
        String username = userAuthProvider.getCurrentUserUsername();
        log.info(username+", addSingleItemToInventory, "+inventoryDto);
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

        String savingPath = "/etc/seller-app/uploads/catalog/images";
        String fileName = Instant.now().getEpochSecond() + "." + getFileExtension(inventoryDto.getFile());

        try {
            File f = new ClassPathResource("").getFile();
            final Path path = Paths.get(savingPath + File.separator + username);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(Objects.requireNonNull(fileName));
            Files.copy(inventoryDto.getFile().getInputStream() , filePath , StandardCopyOption.REPLACE_EXISTING);

            inventory.setFileName(fileName);
            inventory.setFileLocation(savingPath);
            inventoryRepository.save(inventory);
            return new ResponseEntity<>("done" , HttpStatus.OK);

        } catch (IOException e) {
            log.error(username+", error occurred, addSingleItemToInventory, "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public String getFileExtension(MultipartFile file) {
        String username = userAuthProvider.getCurrentUserUsername();
        try{
            log.info(username+", getFileExtension");
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && !originalFilename.isEmpty()) {
                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                    return originalFilename.substring(dotIndex + 1).toLowerCase();
                }
            }
        }catch (Exception e){
            log.error(username+", error occurred, getFileExtension, "+e.getMessage());
        }

        return null;
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

    public ResponseEntity<Page<Inventory>> getAllItems(int page , int size) {

        Pageable pageRequest = PageRequest
                .of(page , size);
        Optional<Page<Inventory>> itemInventory = inventoryRepository
                .findAllBySellerUsernameOrderByIdDesc(userAuthProvider.getCurrentUserUsername() , pageRequest);
        return itemInventory
                .map(listingFileUploads -> new ResponseEntity<>(listingFileUploads , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }

//    private ResponseEntity<Page<Inventory>> mappingCustomFieldsToVariants(int page , int size){
//        Pageable pageRequest = PageRequest
//                .of(page , size);
//        Optional<Page<Inventory>> itemInventory = inventoryRepository
//                .findAllBySellerUsernameOrderByIdDesc(userAuthProvider.getCurrentUserUsername() , pageRequest);
//        if (itemInventory.isPresent()){
////            int customFieldsSize = itemInventory.get().get().collect(Collectors.toList());
//        }
//
//
//    }


}
