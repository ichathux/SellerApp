package com.example.demo.service.impl;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.*;
import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.inventory.*;
import com.example.demo.repository.inventory.*;
import com.example.demo.service.InventoryService;
import com.example.demo.service.SessionUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final VariantRepository variantRepository;
    private final CustomFieldRepository customFieldRepository;
    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final UserAuthProvider userAuthProvider;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final SessionUser sessionUser;

    @Override
    public ResponseEntity<Iterable<Category>> getCategories() {
        return new ResponseEntity<>(categoryRepository.findAll() , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Iterable<SubCategory>> getSubCategories(
            Long category) {
        Optional<Category> category1 = categoryRepository.findById(category);
        Optional<Iterable<SubCategory>> categories = subCategoryRepository.findAllByCategory(category1.orElseThrow());

        return categories
                .map(subCategories -> new ResponseEntity<>(subCategories , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }

    @Override
    public ResponseEntity<Iterable<Brand>> getBrands() {
        return new ResponseEntity<>(brandRepository.findAll() , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<InventoryResponseDto>> getInventoryItemsResponse(
            int page ,
            int size) {

        Optional<Page<Inventory>> itemInventory = inventoryRepository
                .findAllBySellerUsernameOrderByIdDesc(
                        sessionUser.getCurrentUserUsername() ,
                        PageRequest
                                .of(page , size));

        List<InventoryResponseDto> responseList = new ArrayList<>();
        if (itemInventory.isPresent()) {

            List<Inventory> retrievedList = itemInventory.get().getContent();

            for (Inventory inventory : retrievedList) {
                InventoryResponseDto singleResponse = new InventoryResponseDto();
                singleResponse.setId(inventory.getId());
                singleResponse.setVariantsList(manipulateDummyVariantsAsList(inventory.getVariants()));
                singleResponse.setVariants(manipulateVariants(inventory.getVariants()));
                singleResponse.setName(inventory.getName());
                singleResponse.setBrand(inventory.getBrand().getName());
                singleResponse.setSubCategoryId(inventory.getSubCategory());
                singleResponse.setCreatedAt(inventory.getCreatedAt());
                singleResponse.setImage(inventory.getImgUrl());
                singleResponse.setItemDescription(inventory.getItemDescription());
                singleResponse.setPublicId(inventory.getDltUrl());
                singleResponse.setLowestPrice(inventory.getLowestPrice());
                singleResponse.setQty(singleResponse.getQty());
                singleResponse.setImgList(manipulateImgList(inventory));
                responseList.add(singleResponse);
            }
            int totalPages = itemInventory.get().getTotalPages() * itemInventory.get().getSize();
            System.out.println("getSize : "+itemInventory.get().getSize());
            System.out.println("getTotalPages : "+itemInventory.get().getTotalPages());
            System.out.println("getTotalElements : "+itemInventory.get().getTotalElements());
            System.out.println("getPageable : "+itemInventory.get().getPageable());
            return new ResponseEntity<>(new PageImpl<>(
                    responseList ,
                    itemInventory.get().getPageable() ,
                    itemInventory.get().getTotalElements()) ,
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.NO_CONTENT);
    }

    private final ObjectMapper objectMapper;

    private ArrayList<Set<String>> manipulateDummyVariantsAsList(List<Variant> variants) {
        ArrayList<Set<String>> vs = new ArrayList<>();
        for (Variant variant : variants) {
            try {
                String[] v = objectMapper.readValue(variant.getVariants() , String[].class);
                int i = 0;
                for (String s : v) {
                    if (i < vs.size()) {
                        vs.get(i).add(s);
                    } else {
                        Set<String> v2 = new TreeSet<>();
                        v2.add(s);
                        vs.add(i , v2);
                    }
                    i++;
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return vs;
    }

    private ArrayList<VariantResponseDto> manipulateVariants(List<Variant> variants) {
        ArrayList<VariantResponseDto> variantResponseDtos = new ArrayList<>();
        for (Variant variant : variants) {
            try {
                String[] v = objectMapper.readValue(variant.getVariants() , String[].class);
                VariantResponseDto dto = new VariantResponseDto(variant.getId() ,
                        List.of(v) ,
                        variant.getPrice() ,
                        variant.getQty() ,
                        variant.getImgUrl() ,
                        variant.getPublicID() ,
                        variant.isDisable() ,
                        variant.isOutOfStock());
                variantResponseDtos.add(dto);

            } catch (Exception e) {
                log.error("manipulateVariants, " + e.getMessage());
            }
        }
        return variantResponseDtos;
    }

    private Set<String> manipulateImgList(Inventory inventory) {
        Set<String> imgList = new HashSet<>();
        imgList.add(inventory.getImgUrl());
        for (Variant variant : inventory.getVariants()) {
            imgList.add(variant.getImgUrl());
        }
        return imgList;
    }

    @Override
    public ResponseEntity<String> deleteInventoryItem(Long id) {
        log.info(userAuthProvider.getCurrentUserUsername() + ", delete inventory item, " + id);
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()) {
                if (inventory.get().getDltUrl() != null)
                    cloudinaryServiceImpl.removeImg(inventory.get().getDltUrl());
                inventoryRepository.deleteById(id);
            }

            return new ResponseEntity<>("done" , HttpStatus.OK);
        } catch (Exception e) {
            log.error(userAuthProvider.getCurrentUserUsername() + ", error occurred while deleting inventory, " + id);
            return new ResponseEntity<>("error" , HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateInventoryItem(InventoryResponseDto dto) {
        System.out.println("got to back end");
//        System.out.println(dto);

        try {
            Optional<Inventory> relatedInventory = inventoryRepository.findById(dto.getId());
            if (relatedInventory.isPresent()) {
                Inventory inventory = relatedInventory.get();
                inventory.setName(dto.getName());
                Optional<Brand> brand = brandRepository.findByName(dto.getBrand());
                if (brand.isPresent()) {
                    log.info("brand already exist");
                    inventory.setBrand(brand.get());
                } else {
                    log.info("added new brand " + dto.getBrand());
                    String brandName = dto.getBrand();
                    Brand brand1 = new Brand();
                    brand1.setName(brandName);
                    brand1 = brandRepository.save(brand1);
                    inventory.setBrand(brand1);
                }
                inventory.setItemDescription(dto.getItemDescription());
                inventory.setVariants(updateVariantsDetails(dto.getVariants()));
                inventoryRepository.save(inventory);
            } else {
                log.error(sessionUser.getCurrentUserUsername() + " not inventory found for given id " + this.getClass().getName());
                return new ResponseEntity<>("Error" , HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("Done" , HttpStatus.OK);
        } catch (
                Exception e) {
            e.getStackTrace();
            return new ResponseEntity<>("Error" , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private List<Variant> updateVariantsDetails(List<VariantResponseDto> variantDto) {
        List<Variant> variants = new ArrayList<>();
        for (var v : variantDto) {
            Variant variant = variantRepository.findById(v.getId()).orElseThrow(() -> new RuntimeException("No variant found"));
            variant.setQty(v.getQty());
            variant.setPrice(v.getPrice());
            variants.add(variant);

            log.info("updating : "+variant.getVariants());
            log.info("updating : "+variant.getQty());
            log.info("updating : "+variant.getPrice());
        }
        return variants;
    }

    @Override
    public ResponseEntity<String> addInventory(InventoryRequestDto inventoryRequestDto) throws JsonProcessingException {
        log.info("inserting inventory " + inventoryRequestDto);
        try {
            Inventory inventory = new Inventory();
            inventory.setName(inventoryRequestDto.getName());
            Brand brand = new Brand();
            Optional<Brand> brandOpt = brandRepository.findByName(inventoryRequestDto.getBrand());

            if (brandOpt.isPresent()) {
                brand = brandOpt.get();
            } else {
                brand.setName(inventoryRequestDto.getBrand());
                brand = brandRepository.save(brand);
            }

            inventory.setBrand(brand);
            inventory.setItemDescription(inventoryRequestDto.getItemDescription());
            inventory.setImgUrl(inventoryRequestDto.getImgUrl());
            inventory.setDltUrl(inventoryRequestDto.getDelete_url());
            inventory.setStatus(Status.IN_STOCK);
            inventory.setCreatedAt(Instant.now());
            inventory.setSellerUsername(sessionUser.getCurrentUserUsername());
            inventory.setEnabled(true);
            inventory.setLowestPrice(inventoryRequestDto.getLowestPrice());
            inventory.setQty(inventoryRequestDto.getQty());

            Optional<SubCategory> subCategory = subCategoryRepository.findById(inventoryRequestDto.getSubCategoryId());
            subCategory.ifPresent(inventory::setSubCategory);

            List<Variant> variantList = new ArrayList<>();
            for (InventoryVariantDto var : inventoryRequestDto.getVariants()) {
                Variant variant = new Variant();
                ObjectMapper objectMapper = new ObjectMapper();
                String variants = objectMapper.writeValueAsString(var.getVariants());
                variant.setVariants(variants);
                variant.setQty(var.getQty());
                variant.setPrice(var.getPrice());
                variant.setItem(inventory);
                variant.setImgUrl(var.getImgUrl());
                variant.setPublicID(var.getPublic_id());
                variantList.add(variant);
            }
            inventory.setVariants(variantList);
            inventoryRepository.save(inventory);
            return new ResponseEntity<>("Done" ,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.getStackTrace();
            return new ResponseEntity<>(e.getMessage() ,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
