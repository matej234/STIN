package cz.stin.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@ConditionalOnProperty(
        name = "currency.provider",
        havingValue = "real"
)
public class RealCurrencyProvider implements CurrencyProvider {

    @Value("${EXCHANGE_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiCacheService apiCacheService;

    public RealCurrencyProvider(
            ApiCacheService apiCacheService
    ) {
        this.apiCacheService = apiCacheService;
    }

    @Override
    public CurrencyApiResponse getRates() {
        try {
            String url =
                    "https://api.exchangerate.host/live?access_key=" + apiKey;

            CurrencyApiResponse response =
                    restTemplate.getForObject(
                            url,
                            CurrencyApiResponse.class
                    );

            apiCacheService.saveRates(response);

            return response;

        } catch (Exception e) {

            CurrencyApiResponse cached =
                    apiCacheService.loadRates();

            if (cached != null) {
                return cached;
            }

            throw new RuntimeException(
                    "API unavailable and no cached data found"
            );
        }
    }

    @Override
    public CurrencyTimeframeApiResponse getTimeframe(
            String start,
            String end
    ) {
        try {
            String url =
                    "https://api.exchangerate.host/timeframe"
                            + "?access_key=" + apiKey
                            + "&start_date=" + start
                            + "&end_date=" + end;

            CurrencyTimeframeApiResponse response =
                    restTemplate.getForObject(
                            url,
                            CurrencyTimeframeApiResponse.class
                    );

            apiCacheService.saveTimeframe(response);

            return response;

        } catch (Exception e) {

            CurrencyTimeframeApiResponse cached =
                    apiCacheService.loadTimeframe();

            if (cached != null) {
                return cached;
            }

            throw new RuntimeException(
                    "Timeframe API unavailable and no cached data found"
            );
        }
    }
}