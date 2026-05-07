package cz.stin.backend;

import java.util.Map;

public class CurrencyResponse {

    public String source;
    public String base;

    public String strongestCurrency;
    public double strongestValue;

    public String weakestCurrency;
    public double weakestValue;

    public Map<String, Double> rates;

    public CurrencyResponse(
            String source,
            String base,
            String strongestCurrency,
            double strongestValue,
            String weakestCurrency,
            double weakestValue,
            Map<String, Double> rates
    ) {
        this.source = source;
        this.base = base;
        this.strongestCurrency = strongestCurrency;
        this.strongestValue = strongestValue;
        this.weakestCurrency = weakestCurrency;
        this.weakestValue = weakestValue;
        this.rates = rates;
    }
}