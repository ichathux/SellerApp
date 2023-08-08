package com.example.demo.enums;

public enum Status {
    PENDING("Pending"),
    PROCESSING("Processing"),
    UPLOADED("Uploaded"),
    READY_TO_DOWNLOAD("Downloadable"),
    ERROR("Error"),
    ORDER_PLACED("Order placed"),
    PACKED("Packed"),
    COLLECTED_DELIVERY_PARTNER("Collected by delivery partner"),
    COMPLETE("Complete");

    Status(String name) {
    }
}
