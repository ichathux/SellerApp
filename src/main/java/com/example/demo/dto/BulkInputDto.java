package com.example.demo.dto;

import com.example.demo.enums.Status;
import com.example.demo.model.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkInputDto {
    private Instant createdAt;
    private List<Orders> orders;
    private Status status;
    private String username;
}
