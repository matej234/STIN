package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SettingsService {

    private static final String FILE_PATH =
            "backend/data/settings.json";

    private final ObjectMapper mapper =
            new ObjectMapper();

    public UserSettings loadSettings() {
        try {
            return mapper.readValue(
                    new File(FILE_PATH),
                    UserSettings.class
            );

        } catch (IOException e) {
            throw new RuntimeException("Cannot load settings", e);
        }
    }

    public void saveSettings(UserSettings settings) {

        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(FILE_PATH),
                            settings
                    );

        } catch (IOException e) {
            throw new RuntimeException("Cannot save settings", e);
        }
    }
}