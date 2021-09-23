package com.marcelorodrigo.apidifference.model;

public record Difference(long offset, long length) {

    @Override
    public String toString() {
        return "{offset=" + offset + ", length=" + length + '}';
    }
}
