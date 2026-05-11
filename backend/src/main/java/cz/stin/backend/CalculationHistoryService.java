package cz.stin.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationHistoryService {

    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper();

    public CalculationHistoryService() {
        this.filePath =
                Path.of("/home/site/wwwroot/backend/data/calculations.json");
    }

    public void saveCalculation(CalculationRecord record) {
        try {
            File file = new File(filePath.toString());

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, record);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot save calculation history",
                    e
            );
        }
    }

    public CalculationRecord loadHistory() {
        try {
            File file = new File(filePath.toString());

            if (!file.exists() || file.length() == 0) {
                return new CalculationRecord();
            }

            return mapper.readValue(
                    file,
                    CalculationRecord.class
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot load calculation history",
                    e
            );
        }
    }
}