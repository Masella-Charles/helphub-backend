package com.volunteer.main.exceptions;

import com.volunteer.main.model.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    public ProblemDetail handleSecurityException(Exception exception) {
//        ProblemDetail errorDetail = null;
//
//        // TODO send this stack trace to an observability tool
//        exception.printStackTrace();
//
//        if(exception instanceof DataIntegrityViolationException){
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
//            errorDetail.setProperty("description", "Duplicate key violation");
//        }
//
//        if (exception instanceof BadCredentialsException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
//            errorDetail.setProperty("description", "The username or password is incorrect");
//
//            return errorDetail;
//        }
//
//        if (exception instanceof AccountStatusException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "The account is locked");
//        }
//
//        if (exception instanceof AccessDeniedException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "You are not authorized to access this resource");
//        }
//
//        if (exception instanceof SignatureException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "The JWT signature is invalid");
//        }
//
//        if (exception instanceof ExpiredJwtException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "The JWT token has expired");
//        }
//
//        if (errorDetail == null) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
//            errorDetail.setProperty("description", "Unknown internal server error.");
//        }
//
//        return errorDetail;
//    }
//}
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleSecurityException(Exception exception) {
        ErrorResponse errorResponse = null;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // TODO: send this stack trace to an observability tool
        exception.printStackTrace();

        if(exception instanceof EntityNotFoundException){
            status = HttpStatus.NOT_FOUND;
            errorResponse = new ErrorResponse("404", "Record not found", exception.getMessage());
        }

        if (exception instanceof DataIntegrityViolationException) {
            status = HttpStatus.CONFLICT;
            errorResponse = new ErrorResponse("500", "Duplicate key violation", exception.getMessage());
        } else if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            errorResponse = new ErrorResponse("401", "The username or password is incorrect", exception.getMessage());
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse("403", "The account is locked", exception.getMessage());
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse("403", "You are not authorized to access this resource", exception.getMessage());
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse("403", "The JWT signature is invalid", exception.getMessage());
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            errorResponse = new ErrorResponse("403", "The JWT token has expired", exception.getMessage());
        } else {
            errorResponse = new ErrorResponse("500", "Unknown internal server error.", exception.getMessage());
        }

        return errorResponse;
    }
}