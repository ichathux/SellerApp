package com.example.demo.mappers;

import com.example.demo.dto.SellerProfile;
import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    SellerProfile toSellerDetails(SellerDetails sellerDetails);
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
