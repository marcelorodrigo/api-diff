package com.marcelorodrigo.apidifference.util;

import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Base64UtilTest {

    @Test
    public void decodeValidBase64() throws Exception {
        final String originalData = "WAES Assignment\n2019";
        final String encodedBase64 = "V0FFUyBBc3NpZ25tZW50CjIwMTk=";
        assertEquals(originalData, Base64Util.decode(encodedBase64));
    }

    @Test
    public void decodeInvalidBase64() {
        final String invalidData = "SoM3InvaLidD@ta=";
        assertThrows(InvalidBase64Exception.class, () -> Base64Util.decode(invalidData));
    }
}