package cz.stin.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyController {

    private final String API_KEY = "b20811ab87e54b58f99600b4678693f4";

    @GetMapping("/rates")
    public String rates() {

        String url = "https://api.exchangerate.host/live?access_key=" + API_KEY;

        return new RestTemplate().getForObject(url, String.class);
    }
}