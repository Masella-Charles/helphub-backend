package com.volunteer.main.model.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String responseCode;
    private String responseStatus;
    private String additionalInfo;
    // Constructors, getters, and setters
    public ErrorResponse(String responseCode, String responseStatus, String additionalInfo) {
        this.responseCode = responseCode;
        this.responseStatus = responseStatus;
        this.additionalInfo = additionalInfo;
    }


}
