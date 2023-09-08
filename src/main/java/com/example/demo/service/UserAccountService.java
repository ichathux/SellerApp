package com.example.demo.service;

import com.example.demo.dto.CustomFieldDto;
import com.example.demo.model.SellerDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserAccountService {

    ResponseEntity<SellerDetails> getCurrentUserProfile();
    ResponseEntity<SellerDetails> updateSellerDetails(SellerDetails sellerDetails);
    ResponseEntity<Boolean> getCurrentUserInventoryStatus();
    ResponseEntity<String> setInventoryStatus(boolean status);
    ResponseEntity<String> setVariantsForCustomFields(List<String> list , String name);
    ResponseEntity<List<CustomFieldDto>> getCurrentUserVariants();
    ResponseEntity<Map<String, String>> getVariantsLists(String type);

}
