package com.marcelorodrigo.apidifference.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(name = "Base64")
public class Base64VO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "Base64 Binary Data")
    private String data;

    public Base64VO() {
    }

    public Base64VO(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public Base64VO setData(String data) {
        this.data = data;
        return this;
    }
}
