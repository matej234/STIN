package cz.stin.backend;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyServiceTests {

    private final CurrencyService service = new CurrencyService();

    private CurrencyApiResponse mockApi() {
        Map<String, Double> quotes = new LinkedHashMap<>();

        quotes.put("USDEUR", 0.92);
        quotes.put("USDGBP", 0.78);
        quotes.put("USDCZK", 23.1);

        return new CurrencyApiResponse("USD", quotes);
    }

    @Test
    void analyze_basic_case() {
        CurrencyApiResponse api = mockApi();

        CurrencyResponse res = service.analyze(
                api,
                "EUR",
                List.of("EUR", "GBP", "CZK")
        );

        assertEquals("USD", res.source);
        assertEquals("EUR", res.base);

        assertNotNull(res.rates);
        assertTrue(res.rates.containsKey("CZK"));

        assertNotNull(res.strongestCurrency);
        assertNotNull(res.weakestCurrency);
    }

    @Test
    void analyze_should_find_rates_correctly() {
        CurrencyApiResponse api = mockApi();

        CurrencyResponse res = service.analyze(
                api,
                "EUR",
                List.of("EUR", "GBP", "CZK")
        );

        assertEquals("CZK", res.strongestCurrency);
        assertEquals("GBP", res.weakestCurrency);
    }

    @Test
    void analyze_should_skip_unknown_currency() {
        CurrencyApiResponse api = mockApi();

        CurrencyResponse res = service.analyze(
                api,
                "EUR",
                List.of("EUR", "XXX")
        );

        assertTrue(res.rates.containsKey("EUR"));
        assertFalse(res.rates.containsKey("XXX"));
    }

    @Test
    void analyze_should_throw_when_base_missing() {
        CurrencyApiResponse api = mockApi();

        assertThrows(IllegalArgumentException.class, () ->
                service.analyze(api, "ABC", List.of("EUR"))
        );
    }
}