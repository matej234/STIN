package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/current-analysis")
@CrossOrigin
public class CurrentAnalysisHistoryController {

    private final CurrentAnalysisHistoryService service;

    public CurrentAnalysisHistoryController(
            CurrentAnalysisHistoryService service
    ) {
        this.service = service;
    }

    @PostMapping("/save")
    public void save(
            @RequestBody CurrentAnalysisRecord record
    ) {
        service.save(record);
    }

    @GetMapping
    public CurrentAnalysisRecord get() {
        return service.load();
    }
}