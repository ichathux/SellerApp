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
    COMPLETE("Complete"),
    OUT_OF_STOCK("Out of stock"),
    IN_STOCK("In stock");

    Status(String name) {
    }
}
