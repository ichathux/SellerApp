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
    private final CustomFieldRepository customFieldRepository;
    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final UserAuthProvider userAuthProvider;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final SessionUser sessionUser;

//    @Override
//    public ResponseEntity<String> addSingleInventoryItemToDB(
//            InventoryDto inventoryDto) {
//        String username = userAuthProvider.getCurrentUserUsername();
//        log.info(username + ", addSingleItemToInventory, " + inventoryDto);
//        Brand brand = new Brand();
//        Optional<Brand> brandOpt = brandRepository.findByName(inventoryDto.getBrand());
//
//        if (brandOpt.isPresent()) {
//            brand = brandOpt.get();
//        } else {
//            brand.setName(inventoryDto.getBrand());
//            brand = brandRepository.save(brand);
//        }
//
//        Inventory inventory = new Inventory(
//                inventoryDto.getName() ,
//                subCategoryRepository
//                        .findById(inventoryDto.getSubCategoryId())
//                        .orElseThrow(() -> new SpringException("subcategory not found")) ,
//                brand ,
//                null ,
//                inventoryDto.getItemDescription() ,
//                username ,
//                Instant.now() ,
//                Instant.now() ,
//                true ,
//                Status.IN_STOCK ,
//                inventoryDto.getCustomField1() ,
//                inventoryDto.getCustomField1Price() ,
//                inventoryDto.getCustomField2() ,
//                inventoryDto.getCustomField2Price() ,
//                inventoryDto.getCustomField3() ,
//                inventoryDto.getCustomField3Price() ,
//                inventoryDto.getCustomField4() ,
//                inventoryDto.getCustomField4Price() ,
//                inventoryDto.getCustomField5() ,
//                inventoryDto.getCustomField5Price() ,
//                inventoryDto.getCustomField6() ,
//                inventoryDto.getCustomField6Price() ,
//                customFieldRepository
//                        .findByUsernameAndVariantType(username , inventoryDto.getVariantType().trim())
//                        .orElseThrow(() -> new SpringException("Custom field not found")) ,
//                inventoryDto.getImgUrl() ,
//                inventoryDto.getImgUrl() ,
//                inventoryDto.getLowestPrice() ,
//                inventoryDto.getTotalItems());
//
//        inventory.setCustomFieldData(customFieldRepository
//                .findByUsernameAndVariantType(username , inventoryDto.getVariantType().trim())
//                .orElseThrow(() -> new SpringException("Custom field not found")));
//        inventory.setDltUrl(inventoryDto.getDltUrl());
//        inventory.setImgUrl(inventoryDto.getImgUrl());
//
//        inventoryRepository.save(inventory);
//        return new ResponseEntity<>("done" , HttpStatus.OK);
//
//    }

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
            return new ResponseEntity<>(new PageImpl<>(
                    responseList ,
                    itemInventory.get().getPageable() ,
                    totalPages) ,
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.NO_CONTENT);
    }
    private final ObjectMapper objectMapper;

    private ArrayList<Set<String>> manipulateDummyVariantsAsList(List<Variant> variants){
        ArrayList<Set<String>> vs = new ArrayList<>();
        for (Variant variant : variants){
            try {
                String [] v = objectMapper.readValue(variant.getVariants(), String[].class);
                int i = 0;
                for (String s : v){
                    if (i < vs.size()){
                        vs.get(i).add(s);
                    }else{
                        Set<String> v2 = new TreeSet<>();
                        v2.add(s);
                        vs.add(i, v2);
                    }
                    i++;
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return vs;
    }
    private ArrayList <VariantResponseDto> manipulateVariants(List<Variant> variants){
        ArrayList <VariantResponseDto> variantResponseDtos = new ArrayList<>();
        for(Variant variant : variants){
            try{
                String [] v = objectMapper.readValue(variant.getVariants(), String[].class);
                VariantResponseDto dto = new VariantResponseDto(List.of(v),
                        variant.getPrice(),
                        variant.getQty(),
                        variant.getImgUrl(),
                        variant.getPublicID(),
                        variant.getQty() > 0);
                variantResponseDtos.add(dto);

            }catch (Exception e){
                log.error("manipulateVariants, "+e.getMessage());
            }
        }
        return variantResponseDtos;
    }

    private List<String> manipulateImgList(Inventory inventory){
        List<String> imgList = new ArrayList<>();
        imgList.add(inventory.getImgUrl());
        for (Variant variant : inventory.getVariants()){
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
    public ResponseEntity<String> updateInventoryItem(UpdateInventoryDto updateInventoryDto) {
//        Optional<Inventory> inventory = inventoryRepository.findById(updateInventoryDto.getId());
//        if (inventory.isPresent()) {
//            Inventory inventory1 = inventory.get();
//            if (updateInventoryDto.isImageUpdated()) {
//                cloudinaryServiceImpl.removeImg(inventory1.getDltUrl());
//            }
//            inventory1.setName(updateInventoryDto.getName());
//            inventory1.setImgUrl(updateInventoryDto.getImgUrl());
//            inventory1.setDltUrl(updateInventoryDto.getPublicID());
//            inventory1.setItemDescription(updateInventoryDto.getItemDescription());
//            inventory1.setCustomField1(updateInventoryDto.getCustomField1());
//            inventory1.setCustomField1Price(updateInventoryDto.getCustomField1Price());
//            inventory1.setCustomField2(updateInventoryDto.getCustomField2());
//            inventory1.setCustomField2Price(updateInventoryDto.getCustomField2Price());
//            inventory1.setCustomField3(updateInventoryDto.getCustomField3());
//            inventory1.setCustomField3Price(updateInventoryDto.getCustomField3Price());
//            inventory1.setCustomField4(updateInventoryDto.getCustomField4());
//            inventory1.setCustomField4Price(updateInventoryDto.getCustomField4Price());
//            inventory1.setCustomField5(updateInventoryDto.getCustomField5());
//            inventory1.setCustomField5Price(updateInventoryDto.getCustomField5Price());
//            inventory1.setCustomField6(updateInventoryDto.getCustomField6());
//            inventory1.setCustomField6Price(updateInventoryDto.getCustomField6Price());
//            inventoryRepository.save(inventory1);
//
//            return new ResponseEntity<>("Done" , HttpStatus.OK);
//        }
        return new ResponseEntity<>("No Content" , HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> addInventory(InventoryRequestDto inventoryRequestDto) throws JsonProcessingException {
        log.info("inserting inventory "+inventoryRequestDto);
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

//                variant.setDataArray(var.getVariants().toArray(new String[var.getVariants().size()]));
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
            return new ResponseEntity<>(e.getMessage() ,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
