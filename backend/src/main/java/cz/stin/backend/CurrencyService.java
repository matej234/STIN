package cz.stin.backend;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CurrencyService {

    public CurrencyResponse analyze(
            CurrencyApiResponse apiData,
            String base,
            List<String> selectedCurrencies
    ) {
        if (selectedCurrencies == null || selectedCurrencies.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one currency must be selected"
            );
        }
        Map<String, Double> sourceRates = normalizeQuotes(apiData);

        if (!sourceRates.containsKey(base)) {
            throw new IllegalArgumentException("Base currency not found: " + base);
        }

        double baseRate = sourceRates.get(base);

        Map<String, Double> calculatedRates = new LinkedHashMap<>();

        String strongest = null;
        String weakest = null;

        double strongestValue = -Double.MAX_VALUE;
        double weakestValue = Double.MAX_VALUE;

        for (String currency : selectedCurrencies) {

            currency = currency
                    .trim()
                    .replaceAll("\\s+", "")
                    .toUpperCase(Locale.ROOT);

            if (!AllowedCurrencies.LIST.contains(currency)) {
                continue;
            }

            if (!sourceRates.containsKey(currency)) {
                continue;
            }

            double targetRate = sourceRates.get(currency);
            double value = targetRate / baseRate;

            calculatedRates.put(currency, round(value));

            if (value > strongestValue) {
                strongestValue = value;
                strongest = currency;
            }

            if (value < weakestValue) {
                weakestValue = value;
                weakest = currency;
            }
        }

        return new CurrencyResponse(
                apiData.source,
                base,
                strongest,
                round(strongestValue),
                weakest,
                round(weakestValue),
                calculatedRates,
                sourceRates
        );
    }

    public Set<String> getCurrencies(CurrencyApiResponse apiData) {
        Map<String, Double> sourceRates = normalizeQuotes(apiData);

        Set<String> result = new LinkedHashSet<>();

        for (String currency : sourceRates.keySet()) {
            if (AllowedCurrencies.LIST.contains(currency)) {
                result.add(currency);
            }
        }

        return result;
    }

    private Map<String, Double> normalizeQuotes(CurrencyApiResponse apiData) {
        Map<String, Double> result = new LinkedHashMap<>();

        result.put(apiData.source, 1.0);

        for (Map.Entry<String, Double> entry : apiData.quotes.entrySet()) {
            String key = entry.getKey();

            if (key.length() != 6) {
                continue;
            }

            String currency = key.substring(3);
            result.put(currency, entry.getValue());
        }

        return result;
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    public CurrencyTimeframeResponse analyzeTimeframe(
            CurrencyTimeframeApiResponse apiData,
            String base,
            List<String> selectedCurrencies,
            String startDate,
            String endDate
    ) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "Start date must be before end date"
            );
        }

        if (selectedCurrencies == null || selectedCurrencies.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one currency must be selected"
            );
        }

        Map<String, Map<String, Double>> dailyRates = new LinkedHashMap<>();

        Map<String, List<Double>> averageHelper = new HashMap<>();

        for (String date : apiData.quotes.keySet()) {
            LocalDate current = LocalDate.parse(date);

            if (current.isBefore(start) || current.isAfter(end)) {
                continue;
            }

            Map<String, Double> rawDay = apiData.quotes.get(date);

            Map<String, Double> normalizedDay =
                    normalizeDayQuotes(apiData.source, rawDay);

            if (!normalizedDay.containsKey(base)) {
                continue;
            }

            double baseRate = normalizedDay.get(base);

            Map<String, Double> calculatedDay = new LinkedHashMap<>();

            for (String currency : selectedCurrencies) {

                currency = currency.trim();

                if (!AllowedCurrencies.LIST.contains(currency)) {
                    continue;
                }

                if (!normalizedDay.containsKey(currency)) {
                    continue;
                }

                double targetRate = normalizedDay.get(currency);

                double value = targetRate / baseRate;

                value = round(value);

                calculatedDay.put(currency, value);

                averageHelper
                        .computeIfAbsent(currency, k -> new ArrayList<>())
                        .add(value);
            }

            dailyRates.put(date, calculatedDay);
        }

        Map<String, Double> averages = new LinkedHashMap<>();

        for (String currency : averageHelper.keySet()) {

            List<Double> values = averageHelper.get(currency);

            double avg = values.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);

            averages.put(currency, round(avg));
        }

        return new CurrencyTimeframeResponse(
                base,
                dailyRates,
                averages
        );
    }

    private Map<String, Double> normalizeDayQuotes(
            String source,
            Map<String, Double> rawQuotes
    ) {

        Map<String, Double> result = new LinkedHashMap<>();

        result.put(source, 1.0);

        for (Map.Entry<String, Double> entry : rawQuotes.entrySet()) {

            String key = entry.getKey();

            if (key.length() != 6) {
                continue;
            }

            String currency = key.substring(3);

            if (!AllowedCurrencies.LIST.contains(currency)) {
                continue;
            }

            result.put(currency, entry.getValue());
        }

        return result;
    }

    public Set<String> getCurrenciesFromTimeframe(
            CurrencyTimeframeApiResponse apiData
    ) {

        Set<String> result = new LinkedHashSet<>();

        result.add(apiData.source);

        for (Map<String, Double> day : apiData.quotes.values()) {

            for (String key : day.keySet()) {

                if (key.length() == 6) {

                    result.add(key.substring(3));
                }
            }
        }

        return result;
    }
}