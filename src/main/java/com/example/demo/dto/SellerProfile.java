package com.example.demo.dto;

import com.example.demo.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerProfile {
    private String businessName;
    private String ownerName;
    private String address;
    private String contactNo;
//    private String logo;
    private String location;
    private String businessType;
}
