package cz.stin.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsServiceTests {

    @TempDir
    Path tempDir;

    private SettingsService createService() {
        return new SettingsService(tempDir.resolve("settings.json"));
    }

    private UserSettings mockSettings() {
        UserSettings s = new UserSettings();
        s.baseCurrency = "USD";
        s.timeframeBase = "EUR";
        return s;
    }

    @Test
    void save_and_load_should_preserve_values() {
        SettingsService service = createService();

        UserSettings settings = mockSettings();

        service.saveSettings(settings);
        UserSettings loaded = service.loadSettings();

        assertNotNull(loaded);
        assertEquals("USD", loaded.baseCurrency);
        assertEquals("EUR", loaded.timeframeBase);
    }

    @Test
    void load_should_return_default_or_empty_settings_when_file_missing() {
        SettingsService service = createService();

        UserSettings loaded = service.loadSettings();

        assertNotNull(loaded);
    }

    @Test
    void overwrite_should_replace_old_values() {
        SettingsService service = createService();

        UserSettings first = new UserSettings();
        first.baseCurrency = "USD";
        first.timeframeBase = "EUR";

        UserSettings second = new UserSettings();
        second.baseCurrency = "CZK";
        second.timeframeBase = "GBP";

        service.saveSettings(first);
        service.saveSettings(second);

        UserSettings loaded = service.loadSettings();

        assertEquals("CZK", loaded.baseCurrency);
        assertEquals("GBP", loaded.timeframeBase);
    }
}