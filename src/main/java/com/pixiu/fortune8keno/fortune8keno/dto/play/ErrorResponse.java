package com.pixiu.fortune8keno.fortune8keno.dto.play;

import lombok.Data;

@Data
public class ErrorResponse {

    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean ifPresent() {
        return !message.isEmpty();
    }
}
