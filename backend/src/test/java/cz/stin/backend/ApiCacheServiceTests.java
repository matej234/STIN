package cz.stin.backend;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApiCacheServiceTests {

    @Test
    void saveRates_should_create_file() {
        ApiCacheService service =
                new ApiCacheService(
                        "/tmp/test-rates.json",
                        "/tmp/test-timeframe.json"
                );

        Map<String, Double> quotes = new LinkedHashMap<>();
        quotes.put("USDEUR", 0.92);
        quotes.put("USDCZK", 23.1);

        CurrencyApiResponse data =
                new CurrencyApiResponse(
                        "USD",
                        quotes
                );

        service.saveRates(data);

        File file = new File(
                "/tmp/test-rates.json"
        );

        assertTrue(file.exists());
    }

    @Test
    void loadRates_should_return_data_when_file_exists() {
        ApiCacheService service =
                new ApiCacheService(
                        "/tmp/test-rates.json",
                        "/tmp/test-timeframe.json"
                );

        Map<String, Double> quotes = new LinkedHashMap<>();
        quotes.put("USDEUR", 0.92);

        CurrencyApiResponse data =
                new CurrencyApiResponse(
                        "USD",
                        quotes
                );

        service.saveRates(data);

        CurrencyApiResponse loaded =
                service.loadRates();

        assertNotNull(loaded);
        assertEquals("USD", loaded.source);

        assertTrue(
                loaded.quotes.containsKey("USDEUR")
        );
    }

    @Test
    void loadRates_should_return_null_when_file_missing() {
        File file = new File(
                "/tmp/test-rates.json"
        );

        if (file.exists()) {
            file.delete();
        }

        ApiCacheService service =
                new ApiCacheService(
                        "/tmp/test-rates.json",
                        "/tmp/test-timeframe.json"
                );

        CurrencyApiResponse loaded =
                service.loadRates();

        assertNull(loaded);
    }

    @Test
    void saveTimeframe_should_create_file() {
        ApiCacheService service =
                new ApiCacheService(
                        "/tmp/test-rates.json",
                        "/tmp/test-timeframe.json"
                );

        Map<String, Map<String, Double>> quotes =
                new LinkedHashMap<>();

        Map<String, Double> day =
                new LinkedHashMap<>();

        day.put("USDEUR", 0.92);

        quotes.put("2026-05-01", day);

        CurrencyTimeframeApiResponse data =
                new CurrencyTimeframeApiResponse(
                        "USD",
                        quotes
                );

        service.saveTimeframe(data);

        File file = new File(
                "/tmp/test-timeframe.json"
        );

        assertTrue(file.exists());
    }
    @Test
    void loadTimeframe_should_return_data_when_file_exists() {
        ApiCacheService service =
                new ApiCacheService(
                        "/tmp/test-rates.json",
                        "/tmp/test-timeframe.json"
                );

        Map<String, Map<String, Double>> quotes =
                new LinkedHashMap<>();

        Map<String, Double> day =
                new LinkedHashMap<>();

        day.put("USDEUR", 0.92);

        quotes.put("2026-05-01", day);

        CurrencyTimeframeApiResponse data =
                new CurrencyTimeframeApiResponse(
                        "USD",
                        quotes
                );

        service.saveTimeframe(data);

        CurrencyTimeframeApiResponse loaded =
                service.loadTimeframe();

        assertNotNull(loaded);
        assertEquals("USD", loaded.source);

        assertTrue(
                loaded.quotes.containsKey("2026-05-01")
        );
    }
}