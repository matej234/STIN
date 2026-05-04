package cz.stin.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyController {

    @Value("${EXCHANGE_API_KEY}")
    private String API_KEY;

    @GetMapping("/rates")
    public String rates() {

        String url = "https://api.exchangerate.host/live?access_key=" + API_KEY;

        return new RestTemplate().getForObject(url, String.class);
    }

    @GetMapping("/timeframe")
    public String timeframe(
            @RequestParam String start_date,
            @RequestParam String end_date
    ) {

        String url =
                "https://api.exchangerate.host/timeframe"
                        + "?access_key=" + API_KEY
                        + "&start_date=" + start_date
                        + "&end_date=" + end_date;

        return new RestTemplate().getForObject(url, String.class);
    }
}