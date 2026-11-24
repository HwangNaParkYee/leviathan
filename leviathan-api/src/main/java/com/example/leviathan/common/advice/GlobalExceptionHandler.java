package com.example.leviathan.common.advice;

import com.example.leviathan.common.exception.BusinessException;
import com.example.leviathan.common.exception.ErrorCode;
import com.example.leviathan.common.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// Custom Exception
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusiness(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(resolveStatus(errorCode))
                .body(Map.of("code", errorCode.getCode(), "message", errorCode.getMessage()));
    }

    private HttpStatus resolveStatus(ErrorCode errorCode) {
        try {
            String code = errorCode.getCode();
            int start = code.indexOf('-') + 1;
            HttpStatus status = HttpStatus.resolve(Integer.parseInt(code.substring(start, start + 3)));
            return status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (RuntimeException ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
