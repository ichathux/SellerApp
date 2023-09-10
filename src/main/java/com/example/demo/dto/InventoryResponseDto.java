package com.example.demo.dto;

import com.example.demo.model.inventory.SubCategory;
import com.example.demo.model.inventory.Variant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {

    private Long id;
    private String name;
    private SubCategory subCategoryId;
    private String brand;
    private String itemDescription;
    private ArrayList<Set<String>> variantsList;
    private ArrayList<VariantResponseDto> variants;
    private Instant createdAt;
    private String image;
    private String publicId;
    private Double lowestPrice;
    private int qty;
    private Set<String> imgList;

}
