package com.marcelorodrigo.apidifference.util;

import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    public static String decode(final String data) throws InvalidBase64Exception {
        try {
            byte[] original = data.getBytes(StandardCharsets.UTF_8);
            byte[] bytesDecoded = Base64.getDecoder().decode(original);
            return new String(bytesDecoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new InvalidBase64Exception(data);
        }
    }
}
