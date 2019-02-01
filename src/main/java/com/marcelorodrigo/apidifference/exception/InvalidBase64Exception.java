package com.marcelorodrigo.apidifference.exception;

public class InvalidBase64Exception extends Exception {

    public InvalidBase64Exception(String base64Data) {
        super("Invalid base64 data: " + base64Data);
    }
}
