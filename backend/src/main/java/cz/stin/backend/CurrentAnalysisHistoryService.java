package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class CurrentAnalysisHistoryService {

    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper();

    public CurrentAnalysisHistoryService() {
        this.filePath =
                Path.of("/home/site/wwwroot/backend/data/current-analysis.json");
    }

    public void save(CurrentAnalysisRecord record) {
        try {
            File file = new File(filePath.toString());

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, record);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot save current analysis",
                    e
            );
        }
    }

    public CurrentAnalysisRecord load() {
        try {
            File file = new File(filePath.toString());

            if (!file.exists() || file.length() == 0) {
                return new CurrentAnalysisRecord();
            }

            return mapper.readValue(
                    file,
                    CurrentAnalysisRecord.class
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot load current analysis",
                    e
            );
        }
    }
}