package com.example.demo.service.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


//@AllArgsConstructor
@Service
@Getter
public class ApplicationParamService {
    @Value("${app.api.url}")
    private String apiUrl;

    @Value("${app.api.key}")
    private String apiKey;

    @Value("${app.upload.inventory.image.savingPath}")
    private String uploadInventoryImagesSavingPath;

    @Value("${app.upload.inventory.image.retrievingPath}")
    private String uploadInventoryImagesRetrievingPath;

    @Value("${app.upload.order.bulkUpload.savingPath}")
    private String uploadBulkOrdersSavingPath;

    @Value("${app.newuser.default.logo}")
    private String defaultLogoUrl;

    public void doSomething() {
        System.out.println("API URL: " + apiUrl);
        System.out.println("API Key: " + apiKey);
    }
}
