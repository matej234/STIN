package cz.stin.backend;

public interface CurrencyProvider {
    CurrencyApiResponse getRates();
    CurrencyTimeframeApiResponse getTimeframe(String start, String end);
}