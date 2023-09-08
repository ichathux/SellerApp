package com.example.demo.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.config.UserAuthProvider;
import com.example.demo.service.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private UserAuthProvider userAuthProvider;

    @Override
    public void removeImg(String publicId){
        String username = userAuthProvider.getCurrentUserUsername();
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info(username+" ,File deleted successfully, "+publicId);
        } catch (Exception e) {
            log.error(username+" ,Error deleting file, "+publicId);
        }
    }
}
