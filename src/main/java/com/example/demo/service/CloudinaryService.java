package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.config.UserAuthProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private UserAuthProvider userAuthProvider;
    public ResponseEntity<String> removeImg(String publicId){
        String username = userAuthProvider.getCurrentUserUsername();
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info(username+" ,File deleted successfully, "+publicId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            log.error(username+" ,Error deleting file, "+publicId);
            return ResponseEntity.badRequest().body("Error deleting file");
        }
    }
}
