package com.example.demo.dto;

import com.example.demo.model.inventory.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

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
    private List<CustomFieldResponseDto> variants;
    private Instant createdAt;
    private String image;

}
