package com.example.demo.service.impl;

import com.example.demo.config.UserAuthProvider;
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
import lombok.NoArgsConstructor;
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
    private final UserAuthProvider userAuthProvider;

    @Override
    public ResponseEntity<UserDto> login(CredentialDto credentialsDto) {
        log.info("get login request "+credentialsDto);
        User user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user" , HttpStatus.NOT_FOUND));
        UserDto userDto;
        log.info("user exist");
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()) , user.getPassword())) {
            log.info("user password match");
            userDto = userMapper.toUserDto(user);
            String token = userAuthProvider.createToken(userDto);
            userDto.setToken(token);
            String reqToken = getRequestTokenForUser(userDto.getUsername());
            userDto.setRequestToken(reqToken);
            Optional<SellerDetails> sellerDetails = sellerDetailsRepository.findByUsername(credentialsDto.getUsername());
            if (sellerDetails.isPresent()){
                log.info("seller profile exist");
                userDto.setLogo(sellerDetails.get().getLogo());
                userDto.setBusinessName(sellerDetails.get().getDisplayName());
            }

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        log.error("password not matched");
        return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<UserDto> register(SignUpDto userDto) {

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

        UserDto createdUser = userMapper.toUserDto(savedUser);
        String token = userAuthProvider.createToken(createdUser);

        createdUser.setToken(token);
        String reqToken = getRequestTokenForUser(userDto.getUsername());
        createdUser.setRequestToken(reqToken);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
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
    public ResponseEntity<String> deleteUser(String username) {
        try{
            sellerDetailsRepository.deleteByUsername(username);
        }catch (Exception e){
            return new ResponseEntity<>("{\"message\": \""+e.getMessage()+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try{
            userRepository.deleteByUsername(username);
        }catch (Exception e){
            return new ResponseEntity<>("{\"message\": \""+e.getMessage()+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>("{\"message\": \"Deleted!\"}", HttpStatus.OK);

    }
}
