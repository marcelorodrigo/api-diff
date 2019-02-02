package com.marcelorodrigo.apidifference.vo;

import com.marcelorodrigo.apidifference.model.ResultType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

@ApiModel("Difference")
public class DiffResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Result")
    private ResultType resultType;

    @ApiModelProperty("Message")
    private String message;

    public ResultType getResultType() {
        return resultType;
    }

    public DiffResult setResultType(ResultType resultType) {
        if (Objects.nonNull(resultType)) {
            this.setMessage(resultType.getMessage());
        }
        this.resultType = resultType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public DiffResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
