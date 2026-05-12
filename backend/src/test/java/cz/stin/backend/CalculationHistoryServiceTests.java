package cz.stin.backend;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CalculationHistoryServiceTests {

    @Test
    void saveCalculation_should_create_file() {
        Path testPath = Path.of(
                "/tmp/test-calculation-history.json"
        );

        File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        CalculationHistoryService service =
                new CalculationHistoryService(testPath);

        CalculationRecord record =
                new CalculationRecord();

        service.saveCalculation(record);

        assertTrue(file.exists());
    }

    @Test
    void loadHistory_should_return_empty_record_when_file_missing() {
        Path testPath = Path.of(
                "/tmp/test-calculation-history.json"
        );

        File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        CalculationHistoryService service =
                new CalculationHistoryService(testPath);

        CalculationRecord result =
                service.loadHistory();

        assertNotNull(result);
    }

    @Test
    void loadHistory_should_return_saved_record() {
        Path testPath = Path.of(
                "/tmp/test-calculation-history.json"
        );

        CalculationHistoryService service =
                new CalculationHistoryService(testPath);

        CalculationRecord record =
                new CalculationRecord();

        service.saveCalculation(record);

        CalculationRecord loaded =
                service.loadHistory();

        assertNotNull(loaded);
    }
}