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

    private CurrencyTimeframeApiResponse mockTimeframeData() {

        Map<String, Map<String, Double>> quotes = new LinkedHashMap<>();

        Map<String, Double> d1 = new LinkedHashMap<>();
        d1.put("USDEUR", 0.91);
        d1.put("USDGBP", 0.77);
        d1.put("USDCZK", 22.9);

        Map<String, Double> d2 = new LinkedHashMap<>();
        d2.put("USDEUR", 0.92);
        d2.put("USDGBP", 0.78);
        d2.put("USDCZK", 23.1);

        quotes.put("2026-05-01", d1);
        quotes.put("2026-05-02", d2);

        return new CurrencyTimeframeApiResponse("USD", quotes);
    }

    @Test
    void analyze_basic_case() {
        CurrencyResponse res = service.analyze(
                mockApi(),
                "EUR",
                List.of("EUR", "GBP", "CZK")
        );

        assertEquals("USD", res.source);
        assertEquals("EUR", res.base);

        assertFalse(res.rates.isEmpty());
        assertTrue(res.rates.containsKey("CZK"));
    }

    @Test
    void analyze_should_find_strongest_and_weakest() {
        CurrencyResponse res = service.analyze(
                mockApi(),
                "EUR",
                List.of("EUR", "GBP", "CZK")
        );

        assertNotNull(res.strongestCurrency);
        assertNotNull(res.weakestCurrency);

        assertEquals("CZK", res.strongestCurrency);

        assertEquals("GBP", res.weakestCurrency);
    }

    @Test
    void analyze_should_skip_unknown_currency() {
        CurrencyResponse res = service.analyze(
                mockApi(),
                "EUR",
                List.of("EUR", "XXX")
        );

        assertTrue(res.rates.containsKey("EUR"));
        assertFalse(res.rates.containsKey("XXX"));
    }

    @Test
    void analyze_should_throw_when_base_missing() {
        assertThrows(IllegalArgumentException.class, () ->
                service.analyze(mockApi(), "ABC", List.of("EUR"))
        );
    }

    @Test
    void analyze_should_fail_when_list_empty() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyze(
                        mockApi(),
                        "EUR",
                        List.of()
                )
        );
    }

    @Test
    void analyze_should_include_base_currency_as_one() {
        CurrencyResponse res = service.analyze(
                mockApi(),
                "USD",
                List.of("USD", "EUR")
        );

        assertEquals(1.0, res.rates.get("USD"));
    }


    @Test
    void timeframe_should_filter_dates_correctly() {
        CurrencyTimeframeResponse res = service.analyzeTimeframe(
                mockTimeframeData(),
                "USD",
                List.of("EUR"),
                "2026-05-02",
                "2026-05-03"
        );

        assertFalse(res.dailyRates.containsKey("2026-05-01"));
        assertTrue(res.dailyRates.containsKey("2026-05-02"));
    }

    @Test
    void timeframe_should_calculate_averages() {
        CurrencyTimeframeResponse res = service.analyzeTimeframe(
                mockTimeframeData(),
                "USD",
                List.of("EUR"),
                "2026-05-01",
                "2026-05-02"
        );

        assertTrue(res.averages.containsKey("EUR"));
        assertTrue(res.averages.get("EUR") > 0);
    }

    @Test
    void timeframe_should_ignore_unknown_currency() {
        CurrencyTimeframeResponse res = service.analyzeTimeframe(
                mockTimeframeData(),
                "USD",
                List.of("XXX"),
                "2026-05-01",
                "2026-05-02"
        );

        assertTrue(res.dailyRates.values().stream().allMatch(Map::isEmpty));
    }

    @Test
    void analyze_timeframe_should_fail_when_start_after_end() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeTimeframe(
                        mockTimeframeData(),
                        "EUR",
                        List.of("USD", "CZK"),
                        "2024-01-10",
                        "2024-01-01"
                )
        );
    }
}