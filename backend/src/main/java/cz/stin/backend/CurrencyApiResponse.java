package cz.stin.backend;

import java.util.Map;

public class CurrencyApiResponse {

    public String source;
    public Map<String, Double> quotes;

    public CurrencyApiResponse(String source, Map<String, Double> quotes) {
        this.source = source;
        this.quotes = quotes;
    }
}