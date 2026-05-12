package cz.stin.backend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorLogService errorLogService;

    public GlobalExceptionHandler(
            ErrorLogService errorLogService
    ) {
        this.errorLogService = errorLogService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationError(
            IllegalArgumentException e
    ) {
        errorLogService.save(
                "VALIDATION_ERROR",
                e.getMessage()
        );

        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralError(
            Exception e
    ) {
        errorLogService.save(
                "SYSTEM_ERROR",
                e.getMessage()
        );

        return "Unexpected server error";
    }
}