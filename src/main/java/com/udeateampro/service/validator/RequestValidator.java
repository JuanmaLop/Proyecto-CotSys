package com.udeateampro.service.validator;

public class RequestValidator {
    
    private RequestValidator() {
        // Utility class
    }

    public static void validateRequiredString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public static void validatePositiveInteger(Integer value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }
    }

    public static void validateExists(boolean exists, String resourceName, Long id) {
        if (!exists) {
            throw new IllegalArgumentException("No existe " + resourceName + " con id: " + id);
        }
    }

    public static void validateNotEmpty(Object collection, String fieldName) {
        if (collection == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
}
