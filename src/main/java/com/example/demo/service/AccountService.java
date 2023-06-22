package com.example.demo.service;

import com.example.demo.dto.SellerProfile;
import com.example.demo.exception.SpringException;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.repository.SellerDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final AuthService authService;
    private final SellerDetailsRepository sellerDetailsRepository;

    public void completeAccount(SellerProfile sellerProfile){

        User currentUser = authService.getCurrentUser();
        SellerDetails sellerDetails = sellerDetailsRepository
                .findByUser(currentUser).orElseThrow(()-> new SpringException("No such a user"));

        sellerDetails.setAddress(sellerProfile.getAddress());
        sellerDetails.setUser(authService.getCurrentUser());
        sellerDetails.setLogo(sellerProfile.getLogo());
        sellerDetails.setLocation(sellerProfile.getLocation());
        sellerDetails.setContactNo(sellerProfile.getContactNo());
        sellerDetails.setDisplayName(sellerProfile.getBusinessName());
        sellerDetails.setBusinessType(sellerProfile.getBusinessType());
        sellerDetails.setCompleted(true);

        sellerDetailsRepository.save(sellerDetails);

    }
}
