package com.example.demo.model;

import com.example.demo.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
public class ListingFileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String location;
    private Instant dateTime;
    private Status status;
    private String username;
}
