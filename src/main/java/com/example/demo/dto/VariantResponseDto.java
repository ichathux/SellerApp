package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class VariantResponseDto {
    private List<String> name;
    private double price;
    private int qty;
    private String img;
    private String publicId;
    private boolean isAvailable;
}