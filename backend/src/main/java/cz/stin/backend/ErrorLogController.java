package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/error-log")
public class ErrorLogController {

    private final ErrorLogService errorLogService;

    public ErrorLogController(
            ErrorLogService errorLogService
    ) {
        this.errorLogService = errorLogService;
    }

    @PostMapping
    public void save(
            @RequestParam String type,
            @RequestParam String message
    ) {
        errorLogService.save(type, message);
    }
}