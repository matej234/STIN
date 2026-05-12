package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ErrorLogService {

    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper();

    public ErrorLogService() {
        this.filePath =
                Path.of("/home/site/wwwroot/backend/data/error-log.json");
    }

    public void save(String type, String message) {
        try {
            File file = new File(filePath.toString());

            file.getParentFile().mkdirs();

            ErrorLog newLog = new ErrorLog(
                    LocalDateTime.now().toString(),
                    type,
                    message
            );

            List<ErrorLog> logs = new ArrayList<>();

            if (file.exists() && file.length() > 0) {
                logs = Arrays.asList(
                        mapper.readValue(
                                file,
                                ErrorLog[].class
                        )
                );

                logs = new ArrayList<>(logs);
            }

            logs.add(newLog);

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, logs);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot save error log",
                    e
            );
        }
    }
}