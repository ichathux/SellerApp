package com.example.demo.service;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.*;
import com.example.demo.exception.AppException;
import com.example.demo.exception.SpringException;
//import com.example.demo.model.NotificationEmail;
import com.example.demo.mappers.UserMapper;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
//import com.example.demo.security.JwtTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final MailService mailService;
    private final UserMapper userMapper;
//    private final UserAuthProvider userAuthProvider;

    public ResponseEntity<UserDto> login(CredentialDto credentialsDto) {
        User user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user" , HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()) , user.getPassword())) {
            return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {

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
        sellerDetails.setAddress(userDto.getAddress());
        sellerDetails.setDisplayName(userDto.getBusinessName());
        sellerDetails.setCompleted(true);

        sellerDetailsRepository.save(sellerDetails);
        return userMapper.toUserDto(savedUser);
    }
    public String getRequestTokenForUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.get().getRequestToken();
    }
    public UserDto findByLogin(String login) {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }


    public void setRequestTokenForUser(String token , String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            user.get().setRequestToken(token);
            userRepository.save(user.get());
        }
    }
}
