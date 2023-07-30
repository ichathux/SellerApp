package com.example.demo.enums;

public enum Status {
    PENDING("Pending"),
    PROCESSING("Processing"),
    UPLOADED("Uploaded"),
    READY_TO_DOWNLOAD("Downloadable"),
    ERROR("Error");

    Status(String name) {
    }
}
