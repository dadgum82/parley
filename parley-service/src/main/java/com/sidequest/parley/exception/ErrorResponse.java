package com.sidequest.parley.exception;

public class ErrorResponse {
    private final String error;
    private final String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

// --Commented out by Inspection START (11/26/2023 8:06 AM):
// --Commented out by Inspection START (11/26/2023 8:06 AM):
////    public String getError() {
////        return error;
////    }
// --Commented out by Inspection STOP (11/26/2023 8:06 AM)
// --Commented out by Inspection STOP (11/26/2023 8:06 AM)

    public String getMessage() {
        return message;
    }
}