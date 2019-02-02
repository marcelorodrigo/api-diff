package com.marcelorodrigo.apidifference.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("Base64")
public class Base64VO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Base64 Binary Data")
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
