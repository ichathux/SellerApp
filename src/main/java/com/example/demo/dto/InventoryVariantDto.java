package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryVariantDto {
    private String name;
    private List<String> variants;
    private int price;
    private int qty;
    private boolean useImage;
    private String imgUrl;
    private String public_id;
}
