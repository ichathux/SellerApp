package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InventoryRequestDto {
    private String name;
    private String subCategoryId;
    private String brand;
    private String itemDescription;
    private String imgUrl;
    private String delete_url;
    private List<InventoryVariantDto> variants;
    private double lowestPrice;
    private int qty;

}
