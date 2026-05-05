package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyCompareController {

    private final CurrencyService service;

    public CurrencyCompareController(CurrencyService service) {
        this.service = service;
    }

    @PostMapping("/compare")
    public CurrencyResponse compare(@RequestBody CompareRequest req) {

        return service.compare(
                req.base,
                req.selected,
                req.rates,
                req.apiBase
        );
    }

    public static class CompareRequest {
        public String base;
        public String apiBase;
        public List<String> selected;
        public Map<String, Double> rates;
    }
}