package com.marcelorodrigo.apidifference.util;

import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base64UtilTest {

    @Test
    public void decodeValidBase64() throws Exception {
        String originalData = "WAES Assignment\n2019";
        String encodedBase64 = "V0FFUyBBc3NpZ25tZW50CjIwMTk=";
        assertEquals(originalData, Base64Util.decode(encodedBase64));
    }

    @Test(expected = InvalidBase64Exception.class)
    public void decodeInvalidBase64() throws Exception {
        String invalidData = "SoM3InvaLidD@ta=";
        Base64Util.decode(invalidData);
    }
}