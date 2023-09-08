package com.example.demo.controller;

import com.example.demo.dto.CustomFieldDto;
import com.example.demo.dto.SellerProfile;
import com.example.demo.model.SellerDetails;
import com.example.demo.service.UserAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
@Slf4j
public class AccountController {

    private final UserAccountService accountService;
    @PostMapping("completeProfile")
    public ResponseEntity<String> completeAccount(@RequestBody SellerProfile sellerProfile){
        log.info("completing user account  : "+sellerProfile);
        return new ResponseEntity<>("complete", HttpStatus.OK);
    }
    @GetMapping("getUserDetails")
    private ResponseEntity<SellerDetails> getDetails(){
        return accountService.getCurrentUserProfile();
    }
    @PostMapping("addCustomFields")
    private ResponseEntity<String> addCustomFields(@RequestBody CustomFieldDto dto){
        return accountService.setVariantsForCustomFields(dto.getList(), dto.getName());
    }
    @GetMapping("getCustomFields")
    private ResponseEntity<Map<String, String>> getCustomFields(@RequestParam("type") String type){
        return accountService.getVariantsLists(type);
    }
    @GetMapping("getVariantTypes")
    private ResponseEntity<List<CustomFieldDto>> getCurrentUserVariantTypes(){
        return accountService.getCurrentUserVariants();
    }
    @PostMapping("inventoryStatus")
    private ResponseEntity<String> setInventoryStatus(@RequestParam("status") boolean status){
        System.out.println("setting stattus "+status);
        return accountService.setInventoryStatus(status);
    }
    @GetMapping("getInventoryStatus")
    private ResponseEntity<Boolean> getInventoryStatus(){
        return accountService.getCurrentUserInventoryStatus();
    }
    @PostMapping("updateGeneralSettings")
    private ResponseEntity<SellerDetails> updateGeneralSettings(@RequestBody SellerDetails sellerDetails){
        return accountService.updateSellerDetails(sellerDetails);
    }
}
