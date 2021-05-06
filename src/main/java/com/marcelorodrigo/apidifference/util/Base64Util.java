package com.marcelorodrigo.apidifference.util;

import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Base64Util {

    private Base64Util() {
    }

    public static String decode(final String data) throws InvalidBase64Exception {
        try {
            var original = data.getBytes(UTF_8);
            var bytesDecoded = Base64.getDecoder().decode(original);
            return new String(bytesDecoded, UTF_8);
        } catch (Exception e) {
            throw new InvalidBase64Exception(data);
        }
    }
}
