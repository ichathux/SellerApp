package com.example.demo.enums;

public enum Status {
    PENDING("Pending"),
    PROCESSING("Processing"),
    UPLOADED("Uploaded"),
    READY_TO_DOWNLOAD("Downloadable"),
    ERROR("Error"),
    READY_TO_PACK("Ready to packign"),
    PACKED("Packed"),
    COLLECTED_DELIVERY_PARTNER("Collected by delivery partner"),
    COMPLETE("Complete");

    Status(String name) {
    }
}
