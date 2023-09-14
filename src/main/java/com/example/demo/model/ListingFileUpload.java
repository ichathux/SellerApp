package com.example.demo.model;

import com.example.demo.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;
import java.time.Instant;

//@Entity
@Document("listingFileUpload")
@Data
public class ListingFileUpload {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String fileName;
    private String location;
    private Instant dateTime;
    private Status status;
    private String username;
}
