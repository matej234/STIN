package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class SettingsService {

    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper();

    public SettingsService(Path filePath) {
        this.filePath = filePath;
    }

    public SettingsService() {
        this.filePath = Path.of("backend/data/settings.json");
    }

    public UserSettings loadSettings() {
        File file = new File(filePath.toString());

        if (!file.exists()) {
            return new UserSettings();
        }

        try {
            return mapper.readValue(file, UserSettings.class);

        } catch (IOException e) {
            throw new RuntimeException("Cannot load settings", e);
        }
    }

    public void saveSettings(UserSettings settings) {
        try {
            File file = new File(filePath.toString());

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, settings);

        } catch (IOException e) {
            throw new RuntimeException("Cannot save settings", e);
        }
    }
}