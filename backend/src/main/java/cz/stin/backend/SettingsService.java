package cz.stin.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@Service
public class SettingsService {

    private final Path filePath;
    private final ObjectMapper mapper = new ObjectMapper();
    private final boolean useTemplate;

    public SettingsService() {
        this.filePath = Path.of("data/settings.json");
        this.useTemplate = true;
    }

    public SettingsService(Path filePath) {
        this.filePath = filePath;
        this.useTemplate = false;
    }

    public UserSettings loadSettings() {
        File file = new File(filePath.toString());

        if (!file.exists()) {

            if (useTemplate) {
                copyDefaultSettingsFile();
            } else {
                return new UserSettings();
            }
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

    private void copyDefaultSettingsFile() {
        try {
            File file = new File(filePath.toString());

            file.getParentFile().mkdirs();

            InputStream input =
                    new ClassPathResource("data/settings.json")
                            .getInputStream();

            Files.copy(
                    input,
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot create default settings file",
                    e
            );
        }
    }
}