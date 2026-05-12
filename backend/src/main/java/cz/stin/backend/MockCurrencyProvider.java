package cz.stin.backend;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ConditionalOnProperty(
        name = "currency.provider",
        havingValue = "mock",
        matchIfMissing = true
)
public class MockCurrencyProvider implements CurrencyProvider {

    @Override
    public CurrencyApiResponse getRates() {

        Map<String, Double> quotes = new LinkedHashMap<>();

        quotes.put("USDEUR", 0.92);
        quotes.put("USDGBP", 0.78);
        quotes.put("USDCZK", 23.1);
        quotes.put("USDAUD", 1.52);
        quotes.put("USDJPY", 150.4);

        return new CurrencyApiResponse("USD", quotes);
    }

    @Override
    public CurrencyTimeframeApiResponse getTimeframe(String start, String end) {

        Map<String, Map<String, Double>> quotes = new LinkedHashMap<>();

        Map<String, Double> d1 = new LinkedHashMap<>();
        d1.put("USDEUR", 0.91);
        d1.put("USDCZK", 22.9);

        Map<String, Double> d2 = new LinkedHashMap<>();
        d2.put("USDEUR", 0.92);
        d2.put("USDCZK", 23.1);

        Map<String, Double> d3 = new LinkedHashMap<>();
        d3.put("USDEUR", 0.93);
        d3.put("USDCZK", 23.3);

        quotes.put("2026-05-01", d1);
        quotes.put("2026-05-02", d2);
        quotes.put("2026-05-03", d3);

        return new CurrencyTimeframeApiResponse("USD", quotes);
    }
}