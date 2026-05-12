package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

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

            ErrorLog log = new ErrorLog(
                    LocalDateTime.now().toString(),
                    type,
                    message
            );

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, log);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot save error log",
                    e
            );
        }
    }
}