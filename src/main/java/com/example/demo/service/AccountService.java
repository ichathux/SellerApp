package com.example.demo.service;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.SellerProfile;
import com.example.demo.exception.SpringException;
import com.example.demo.mappers.UserDetailsMapper;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final UserRepository userRepository;
    private final UserAuthProvider userAuthProvider;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final UserDetailsMapper userDetailsMapper;
//    public void completeAccount(SellerProfile sellerProfile){
//
//        User currentUser = authService.getCurrentUser();
//        SellerDetails sellerDetails = sellerDetailsRepository
//                .findByUser(currentUser).orElseThrow(()-> new SpringException("No such a user"));
//
//        sellerDetails.setAddress(sellerProfile.getAddress());
//        sellerDetails.setUser(authService.getCurrentUser());
////        sellerDetails.setLogo(sellerProfile.getLogo());
//        sellerDetails.setLocation(sellerProfile.getLocation());
//        sellerDetails.setContactNo(Long.valueOf(sellerProfile.getContactNo()));
//        sellerDetails.setDisplayName(sellerProfile.getBusinessName());
////        sellerDetails.setBusinessType(sellerProfile.getBusinessType());
//        sellerDetails.setCompleted(true);
//
//        sellerDetailsRepository.save(sellerDetails);
//
//    }

    public ResponseEntity<SellerProfile> getUserProfile() {
        Optional<SellerDetails> sellerDetails = sellerDetailsRepository
                .findByUsername(userAuthProvider.getCurrentUserUsername());

//        System.out.println("mapping results " + );
        SellerProfile sellerProfile = userDetailsMapper.toSellerProfile(sellerDetails.get());

        return sellerDetails
                .map(details -> new ResponseEntity<>(userDetailsMapper.toSellerProfile(details) , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new SellerProfile() , HttpStatus.NOT_FOUND));

    }
}
