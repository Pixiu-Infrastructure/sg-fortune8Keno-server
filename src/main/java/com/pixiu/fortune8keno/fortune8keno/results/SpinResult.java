package com.pixiu.fortune8keno.fortune8keno.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pixiu.fortune8keno.fortune8keno.dto.play.ErrorResponse;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpinResult {


    private double prizeAmount;

    private StateResult state = new StateResult();
    private CommandResult result = new CommandResult();


    private String action;
    private Double winAmount;

    private ErrorResponse error;

    @JsonProperty("isRoundComplete")
    private boolean roundDone;


    public static SpinResult error(String action, String code, String message) {
        SpinResult response = new SpinResult();
        response.setAction(action);
        response.setError(new ErrorResponse(code, message));
        return response;
    }







}
