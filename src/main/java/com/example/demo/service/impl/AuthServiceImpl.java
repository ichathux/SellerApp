package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.AppException;
//import com.example.demo.model.NotificationEmail;
import com.example.demo.mappers.UserMapper;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final UserMapper userMapper;
    private final ApplicationParamService applicationParamService;

    @Override
    public ResponseEntity<UserDto> login(CredentialDto credentialsDto) {
        User user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user" , HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()) , user.getPassword())) {
            return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    @Override
    public UserDto register(SignUpDto userDto) {

        System.out.println(userDto);
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.CONFLICT);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        user.setUsername(userDto.getUsername());
        user.setCreated(Instant.now());

        SellerDetails sellerDetails = new SellerDetails();
        User savedUser = userRepository.save(user);
        sellerDetails.setUsername(savedUser.getUsername());
        sellerDetails.setOwnerName(userDto.getFirstName()+" "+userDto.getLastName());
        sellerDetails.setContactNo(Long.valueOf(userDto.getContact()));
        sellerDetails.setCountry(userDto.getCountry());
        sellerDetails.setDisplayName(userDto.getBusinessName());
        sellerDetails.setCompleted(true);
        sellerDetails.setLogo(applicationParamService.getDefaultLogoUrl());

        sellerDetailsRepository.save(sellerDetails);
        return userMapper.toUserDto(savedUser);
    }
    @Override
    public String getRequestTokenForUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.get().getRequestToken();
    }
    @Override
    public UserDto findByLogin(String login) {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
    @Override
    public void setRequestTokenForUser(String token , String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            user.get().setRequestToken(token);
            userRepository.save(user.get());
        }
    }
}
