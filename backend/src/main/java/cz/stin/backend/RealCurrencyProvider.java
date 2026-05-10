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

    @Override
    public CurrencyApiResponse getRates() {

        String url =
                "https://api.exchangerate.host/live?access_key=" + apiKey;

        String json = restTemplate.getForObject(url, String.class);

        throw new UnsupportedOperationException("Parse JSON → CurrencyApiResponse");
    }

    @Override
    public CurrencyTimeframeApiResponse getTimeframe(String start, String end) {

        String url =
                "https://api.exchangerate.host/timeframe"
                        + "?access_key=" + apiKey
                        + "&start_date=" + start
                        + "&end_date=" + end;

        String json = restTemplate.getForObject(url, String.class);

        throw new UnsupportedOperationException("Parse JSON → TimeframeResponse");
    }
}