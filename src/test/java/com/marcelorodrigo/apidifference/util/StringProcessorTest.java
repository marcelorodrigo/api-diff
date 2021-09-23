package com.marcelorodrigo.apidifference.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringProcessorTest {

    @Test
    void getDifferences() {
        final var left = "Marcelo Rodrigo";
        final var right = "MarXYlo RoTrig9";
        final var differences = StringProcessor.getDifferences(left, right);
        assertEquals(3, differences.size());

        // First difference
        assertEquals(2, differences.get(0).getLength());
        assertEquals(3, differences.get(0).getOffset());

        // Second difference
        assertEquals(1, differences.get(1).getLength());
        assertEquals(10, differences.get(1).getOffset());

        // Third difference
        assertEquals(1, differences.get(2).getLength());
        assertEquals(14, differences.get(2).getOffset());
    }
}