package cz.stin.backend;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Login {

    @PostMapping("/login")
    public String login(@RequestBody String username) {

        if (username != null && !username.isEmpty()) {
            return "OK";
        }


        return "FAIL";
    }
}