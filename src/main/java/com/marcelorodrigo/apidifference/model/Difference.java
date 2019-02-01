package com.marcelorodrigo.apidifference.model;

public class Difference {

    private long offset;
    private long length;

    public Difference(long offset, long length) {
        this.offset = offset;
        this.length = length;
    }

    public long getOffset() {
        return offset;
    }

    public long getLength() {
        return length;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{offset=").append(offset);
        sb.append(", length=").append(length);
        sb.append('}');
        return sb.toString();
    }
}
