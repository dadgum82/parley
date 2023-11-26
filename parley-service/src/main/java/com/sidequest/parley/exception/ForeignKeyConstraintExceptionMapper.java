package com.sidequest.parley.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForeignKeyConstraintExceptionMapper implements ExceptionMapper<ForeignKeyConstraintException> {

    @Override
    public Response toResponse(ForeignKeyConstraintException exception) {
        // Create a custom error response object or JSON payload with the error message
        ErrorResponse errorResponse = new ErrorResponse("Foreign key constraint failed",
                "The requested operation cannot be completed due to a foreign key constraint failure.");

        // Set the appropriate HTTP status code
        int statusCode = 422; // Unprocessable Entity

        // Return the response with the error object and status code
        //return new ErrorResponse(statusCode, errorResponse).build();
        return Response.status(statusCode).entity(errorResponse).build();
    }
}

