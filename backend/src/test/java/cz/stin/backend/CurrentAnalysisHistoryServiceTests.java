package cz.stin.backend;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CurrentAnalysisHistoryServiceTests {

    @Test
    void load_should_return_empty_record_when_file_missing() {
        Path testPath = Path.of(
                "/tmp/test-current-analysis.json"
        );

        File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        CurrentAnalysisHistoryService service =
                new CurrentAnalysisHistoryService(testPath);

        CurrentAnalysisRecord result =
                service.load();

        assertNotNull(result);
    }

    @Test
    void save_should_create_file() {
        Path testPath = Path.of(
                "/tmp/test-current-analysis.json"
        );

        File file = testPath.toFile();

        if (file.exists()) {
            file.delete();
        }

        CurrentAnalysisHistoryService service =
                new CurrentAnalysisHistoryService(testPath);

        CurrentAnalysisRecord record =
                new CurrentAnalysisRecord();

        service.save(record);

        assertTrue(file.exists());
    }

    @Test
    void load_should_return_saved_data() {
        Path testPath = Path.of(
                "/tmp/test-current-analysis.json"
        );

        CurrentAnalysisHistoryService service =
                new CurrentAnalysisHistoryService(testPath);

        CurrentAnalysisRecord record =
                new CurrentAnalysisRecord();

        service.save(record);

        CurrentAnalysisRecord loaded =
                service.load();

        assertNotNull(loaded);
    }
}