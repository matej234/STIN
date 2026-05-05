package cz.stin.backend;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyService {

    public CurrencyResponse compare(String base, List<String> selected, Map<String, Double> rates, String apiBase) {

        CurrencyResponse res = new CurrencyResponse();
        res.base = base;

        double baseRate = getRate(base, apiBase, rates);
        if (baseRate == 0) return res;

        List<CurrencyResponse.Item> list = new ArrayList<>();

        CurrencyResponse.Item strongest = null;
        CurrencyResponse.Item weakest = null;

        for (String c : selected) {

            double rate = getRate(c, apiBase, rates);

            if (rate == 0) continue;

            double value = rate / baseRate;

            CurrencyResponse.Item item = new CurrencyResponse.Item(c, value);
            list.add(item);

            if (strongest == null || value < strongest.value)
                strongest = item;

            if (weakest == null || value > weakest.value)
                weakest = item;
        }

        res.rates = list;
        res.strongest = strongest;
        res.weakest = weakest;

        return res;
    }

    private double getRate(String currency, String apiBase, Map<String, Double> rates) {
        if (currency.equals(apiBase)) return 1;

        return rates.getOrDefault(apiBase + currency, 0.0);
    }
}