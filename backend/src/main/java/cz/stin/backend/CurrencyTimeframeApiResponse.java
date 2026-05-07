package cz.stin.backend;

import java.util.Map;

public class CurrencyTimeframeApiResponse {

    public String source;

    public Map<String, Map<String, Double>> quotes;

    public CurrencyTimeframeApiResponse(
            String source,
            Map<String, Map<String, Double>> quotes
    ) {
        this.source = source;
        this.quotes = quotes;
    }
}