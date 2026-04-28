package cz.stin.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyController {

    private final String API_KEY = "2d5cba819cfc2e2a9f91df3356e88cf8";

    @GetMapping("/list")
    public String list() {
        String url = "https://api.exchangerate.host/list?access_key=" + API_KEY;
        return new RestTemplate().getForObject(url, String.class);
    }
}