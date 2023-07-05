package com.example.demo.service;

import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.SpringException;
//import com.example.demo.model.NotificationEmail;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.security.JwtTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final MailService mailService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        SellerDetails sellerDetails = new SellerDetails();
        sellerDetails.setUser(userRepository.save(user));
        sellerDetailsRepository.save(sellerDetails);


        String token = generateVerificationToken(user);

        mailService.sendEmail(new NotificationEmail("Please activate your account" ,
                user.getUsername() , "Thank you for signing up to Spring Reddit " +
                "plese click on the below url to activate you account :" +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        log.info("verifying token " + token);
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        log.info("verifying user " + verificationToken.get().getUser().getUsername());
        verificationToken.orElseThrow(() -> new SpringException("Invalid Token"));
        log.info("fetching user " + verificationToken.get().getUser().getUsername());
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        log.info("fetching user and enabling");
        String username = verificationToken.getUser().getUsername();
        log.info("enabling user " + username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringException("User Not Fount"));
        log.info("enabling user " + user.getUsername());
        user.setEnabled(true);
        userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                , loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .sellerDetails(sellerDetailsRepository.findByUser(getCurrentUser()).get())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        log.info("getting current user");
        log.info("getting current user : " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info(principal.getUsername());
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        refreshTokenService.validateRefreshToken(refreshToken.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshToken.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshToken.getUsername())
                .build();
    }

    public boolean checkUserAlreadyExist(String username){
        Optional<User> alreadyRegistered = userRepository.findByUsername(username);
        if (alreadyRegistered.isPresent()){
            return true;
        }else {
            return false;
        }
    }

}
