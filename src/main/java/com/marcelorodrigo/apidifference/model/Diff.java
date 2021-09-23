package com.marcelorodrigo.apidifference.model;

public class Diff {
    private final String id;
    private String left;
    private String right;

    public Diff(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLeft() {
        return left;
    }

    public Diff setLeft(String left) {
        this.left = left;
        return this;
    }

    public String getRight() {
        return right;
    }

    public Diff setRight(String right) {
        this.right = right;
        return this;
    }
}
