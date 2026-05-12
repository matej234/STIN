package cz.stin.backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTests {

    @Test
    void handleValidationError_should_return_message() {
        ErrorLogService errorLogService =
                new ErrorLogService(
                        java.nio.file.Path.of(
                                "/tmp/test-global-error-log.json"
                        )
                );

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler(
                        errorLogService
                );

        String result =
                handler.handleValidationError(
                        new IllegalArgumentException(
                                "Start date must be before end date"
                        )
                );

        assertEquals(
                "Start date must be before end date",
                result
        );
    }

    @Test
    void handleGeneralError_should_return_generic_message() {
        ErrorLogService errorLogService =
                new ErrorLogService(
                        java.nio.file.Path.of(
                                "/tmp/test-global-error-log.json"
                        )
                );

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler(
                        errorLogService
                );

        String result =
                handler.handleGeneralError(
                        new RuntimeException(
                                "Database failed"
                        )
                );

        assertEquals(
                "Unexpected server error",
                result
        );
    }

    @Test
    void handleValidationError_should_save_log() {
        java.nio.file.Path testPath =
                java.nio.file.Path.of(
                        "/tmp/test-global-error-log-save.json"
                );

        java.io.File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        ErrorLogService errorLogService =
                new ErrorLogService(testPath);

        GlobalExceptionHandler handler =
                new GlobalExceptionHandler(
                        errorLogService
                );

        handler.handleValidationError(
                new IllegalArgumentException(
                        "Validation failed"
                )
        );

        assertTrue(file.exists());
    }
}