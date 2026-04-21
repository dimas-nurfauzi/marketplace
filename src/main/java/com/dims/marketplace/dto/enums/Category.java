package com.dims.marketplace.dto.enums;

public enum Category {
    ELECTRONICS,
    FASHION,
    FOOD,
    BOOK,
    OTHER;

    public String getDisplayName() {
        return switch (this) {
            case ELECTRONICS -> "Electronics";
            case FASHION -> "Fashion";
            case FOOD -> "Food";
            case BOOK -> "Book";
            case OTHER -> "Other";
        };
    }
}