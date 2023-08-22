package com.example.demo.dto;

import com.example.demo.model.inventory.Brand;
import com.example.demo.model.inventory.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.File;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private String name;
    private Long subCategoryId;
    private String brand;
    private String itemDescription;
    //    private MultipartFile file;
    private Long customField1 = 0L;
    private Double customField1Price = 0D;
    private Long customField2 = 0L;
    private Double customField2Price = 0D;
    private Long customField3 = 0L;
    private Double customField3Price = 0D;
    private Long customField4 = 0L;
    private Double customField4Price = 0D;
    private Long customField5 = 0L;
    private Double customField5Price = 0D;
    private Long customField6 = 0L;
    private Double customField6Price = 0D;
    private String variantType;
    private String imgUrl;
}
