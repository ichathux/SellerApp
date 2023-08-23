package com.example.demo.dto;

import com.example.demo.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SellerProfile {
    private Long id;
    private Long contactNo;
    private String username;
    private String ownerName;
    private String displayName;
    private String address;
    private String logo;
    private String location;
    private boolean isCompleted;
    private String country;
    private String nic;
    private String description;
}
