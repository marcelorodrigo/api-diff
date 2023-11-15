package com.marcelorodrigo.apidifference.vo;

import com.marcelorodrigo.apidifference.model.ResultType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "Difference")
public class DiffResult {

    @Schema(name = "Result")
    private ResultType resultType;

    @Schema(name = "Message")
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
