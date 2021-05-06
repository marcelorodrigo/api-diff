package com.marcelorodrigo.apidifference.util;

import com.marcelorodrigo.apidifference.model.Difference;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringProcessorTest {

    @Test
    public void getDifferences() {
        final String left = "Marcelo Rodrigo";
        final String right = "MarXYlo RoTrig9";

        List<Difference> differences = StringProcessor.getDifferences(left, right);
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