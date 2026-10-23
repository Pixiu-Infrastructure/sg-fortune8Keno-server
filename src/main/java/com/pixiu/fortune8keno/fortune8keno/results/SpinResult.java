package com.pixiu.fortune8keno.fortune8keno.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pixiu.fortune8keno.fortune8keno.dto.play.ErrorResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpinResult {


    private boolean isRoundComplete;
    private BigDecimal prizeAmount;

    private StateResult state = new StateResult();
    private CommandResult result = new CommandResult();


    private String action;
    private BigDecimal winAmount;

    private ErrorResponse error;


    public static SpinResult error(String action, String code, String message) {
        SpinResult response = new SpinResult();
        response.setAction(action);
        response.setError(new ErrorResponse(code, message));
        return response;
    }







}
