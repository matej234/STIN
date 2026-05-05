package cz.stin.backend;

import java.util.List;

public class CurrencyResponse {

    public String base;

    public Item strongest;
    public Item weakest;

    public List<Item> rates;

    public static class Item {
        public String currency;
        public double value;

        public Item(String currency, double value) {
            this.currency = currency;
            this.value = value;
        }
    }
}