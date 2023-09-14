package com.example.demo.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
@Document("customFieldData")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomFieldData {

    @Id
    private String id;
    private String username;
    private String customField1;
    private String customField2;
    private String customField3;
    private String customField4;
    private String customField5;
    private String customField6;
    private int fieldsUsed;
    private String variantType;
}
