package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin
public class CalculationHistoryController {

    private final CalculationHistoryService service;

    public CalculationHistoryController(
            CalculationHistoryService service
    ) {
        this.service = service;
    }

    @PostMapping("/save")
    public void saveCalculation(
            @RequestBody CalculationRecord record
    ) {
        service.saveCalculation(record);
    }

    @GetMapping
    public CalculationRecord getHistory() {
        return service.loadHistory();
    }
}