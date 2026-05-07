package cz.stin.backend;

import java.util.Map;

public class CurrencyTimeframeResponse {

    public String base;

    public Map<String, Map<String, Double>> dailyRates;

    public Map<String, Double> averages;

    public CurrencyTimeframeResponse(
            String base,
            Map<String, Map<String, Double>> dailyRates,
            Map<String, Double> averages
    ) {
        this.base = base;
        this.dailyRates = dailyRates;
        this.averages = averages;
    }
}