package com.marcelorodrigo.apidifference.model;

public enum ResultType {
    EQUALS("Values are equal"),
    DIFFERENT_LENGTH("Values are of different length"),
    SAME_LENGTH("Values have same length"),
    INVALID_DATA("Invalid data");

    private final String message;

    ResultType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
