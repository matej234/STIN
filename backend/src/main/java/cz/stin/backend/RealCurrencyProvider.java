package cz.stin.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("prod")
public class RealCurrencyProvider implements CurrencyProvider {

    @Value("${EXCHANGE_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public CurrencyApiResponse getRates() {

        String url =
                "https://api.exchangerate.host/live?access_key=" + apiKey;

        String json = restTemplate.getForObject(url, String.class);

        // 🔴 sem bys měl dát Jackson parsing
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