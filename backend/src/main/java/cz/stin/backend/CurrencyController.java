package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyController {

    private final CurrencyProvider provider;
    private final CurrencyService service;

    public CurrencyController(CurrencyProvider provider, CurrencyService service) {
        this.provider = provider;
        this.service = service;
    }

    @GetMapping("/analyze")
    public CurrencyResponse analyze(
            @RequestParam(defaultValue = "EUR") String base,
            @RequestParam(required = false) String currencies
    ) {

        CurrencyApiResponse apiData = provider.getRates();

        List<String> list =
                (currencies == null || currencies.isBlank())
                        ? new ArrayList<>(service.getCurrencies(apiData))
                        : Arrays.asList(currencies.split(","));

        return service.analyze(apiData, base, list);
    }

    @GetMapping("/currencies")
    public Set<String> currencies() {
        return service.getCurrencies(provider.getRates());
    }

    @GetMapping("/timeframe")
    public CurrencyTimeframeResponse timeframe(
            @RequestParam String base,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String currencies
    ) {

        CurrencyTimeframeApiResponse apiData =
                provider.getTimeframe(startDate, endDate);

        List<String> list =
                (currencies == null || currencies.isBlank())
                        ? new ArrayList<>(service.getCurrenciesFromTimeframe(apiData))
                        : Arrays.asList(currencies.split(","));

        return service.analyzeTimeframe(
                apiData,
                base,
                list,
                startDate,
                endDate
        );
    }
}