package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorLogServiceTests {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void save_should_append_multiple_logs() throws Exception {
        Path testPath = Path.of(
                "/tmp/test-error-log-append.json"
        );

        File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        ErrorLogService service =
                new ErrorLogService(testPath);

        service.save(
                "VALIDATION_ERROR",
                "First message"
        );

        service.save(
                "SYSTEM_ERROR",
                "Second message"
        );

        assertTrue(file.exists());

        ErrorLog[] logs =
                mapper.readValue(
                        file,
                        ErrorLog[].class
                );

        assertEquals(2, logs.length);
    }

    @Test
    void saved_file_should_be_valid_json_array() throws Exception {
        Path testPath = Path.of(
                "/tmp/test-error-log-valid.json"
        );

        File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        ErrorLogService service =
                new ErrorLogService(testPath);

        service.save(
                "SYSTEM_ERROR",
                "Validation test"
        );

        assertTrue(file.exists());

        ErrorLog[] logs =
                mapper.readValue(
                        file,
                        ErrorLog[].class
                );

        assertNotNull(logs);
        assertTrue(logs.length > 0);
    }
}