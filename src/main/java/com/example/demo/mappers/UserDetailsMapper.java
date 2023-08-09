package com.example.demo.mappers;

import com.example.demo.dto.SellerProfile;
import com.example.demo.model.SellerDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {
    SellerProfile toSellerProfile(SellerDetails sellerDetails);
}
