package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
public class UpdateInventoryDto {

    private  Long id;
    private String name;
    private String itemDescription;
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
    private String imgUrl;
    private String publicID;
    private boolean isImageUpdated;
}
