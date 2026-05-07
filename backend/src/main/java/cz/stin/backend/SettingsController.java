package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class SettingsController {

    private final SettingsService service;

    public SettingsController(SettingsService service) {
        this.service = service;
    }

    @GetMapping
    public UserSettings getSettings() {
        return service.loadSettings();
    }

    @PostMapping
    public void saveSettings(
            @RequestBody UserSettings settings
    ) {
        service.saveSettings(settings);
    }
}