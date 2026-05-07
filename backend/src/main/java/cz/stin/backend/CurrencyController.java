package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping("/analyze")
    public CurrencyResponse analyze(
            @RequestParam(defaultValue = "EUR") String base,
            @RequestParam(required = false) String currencies
    ) {
        CurrencyApiResponse apiData = mockApiData();

        List<String> currencyList;

        if (currencies == null || currencies.isBlank()) {
            currencyList = new ArrayList<>(service.getCurrencies(apiData));
        } else {
            currencyList = Arrays.asList(currencies.split(","));
        }

        return service.analyze(apiData, base, currencyList);
    }

    @GetMapping("/currencies")
    public Set<String> currencies() {
        CurrencyApiResponse apiData = mockApiData();
        return service.getCurrencies(apiData);
    }

    @GetMapping("/timeframe")
    public CurrencyTimeframeResponse timeframe(
            @RequestParam String base,
            @RequestParam String start_date,
            @RequestParam String end_date,
            @RequestParam(required = false) String currencies
    ) {

        CurrencyTimeframeApiResponse apiData = mockTimeframeData();

        List<String> currencyList;

        if (currencies == null || currencies.isBlank()) {

            currencyList = new ArrayList<>(
                    service.getCurrenciesFromTimeframe(apiData)
            );

        } else {

            currencyList = Arrays.asList(currencies.split(","));
        }

        return service.analyzeTimeframe(
                apiData,
                base,
                currencyList
        );
    }

    private CurrencyApiResponse mockApiData() {
        Map<String, Double> quotes = new LinkedHashMap<>();

        quotes.put("USDEUR", 0.92);
        quotes.put("USDGBP", 0.78);
        quotes.put("USDCZK", 23.1);
        quotes.put("USDAUD", 1.52);
        quotes.put("USDJPY", 150.4);
        quotes.put("USDXYZ", 99.99);

        return new CurrencyApiResponse("USD", quotes);
    }

    private CurrencyTimeframeApiResponse mockTimeframeData() {

        Map<String, Map<String, Double>> quotes =
                new LinkedHashMap<>();

        Map<String, Double> d1 = new LinkedHashMap<>();
        d1.put("USDEUR", 0.91);
        d1.put("USDGBP", 0.77);
        d1.put("USDCZK", 22.9);
        d1.put("USDXYZ", 99.99);

        Map<String, Double> d2 = new LinkedHashMap<>();
        d2.put("USDEUR", 0.92);
        d2.put("USDGBP", 0.78);
        d2.put("USDCZK", 23.1);
        d2.put("USDXYZ", 99.99);

        Map<String, Double> d3 = new LinkedHashMap<>();
        d3.put("USDEUR", 0.93);
        d3.put("USDGBP", 0.79);
        d3.put("USDCZK", 23.3);
        d3.put("USDXYZ", 99.99);

        quotes.put("2026-05-01", d1);
        quotes.put("2026-05-02", d2);
        quotes.put("2026-05-03", d3);

        return new CurrencyTimeframeApiResponse(
                "USD",
                quotes
        );
    }
}