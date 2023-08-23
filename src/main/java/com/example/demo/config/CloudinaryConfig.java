package com.example.demo.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary("cloudinary://728923541341992:HfNKW41h75c-5gF_VOs3bquJ6tk@ddzs2ixbq");
    }
}
