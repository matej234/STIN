package cz.stin.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Login {

    private final String HASHED_PASSWORD = BCrypt.hashpw("MATEJ", BCrypt.gensalt());

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {

        if (req.username == null || req.password == null) {
            return "FAIL";
        }

        if (req.username.equals("Matěj") &&
                BCrypt.checkpw(req.password, HASHED_PASSWORD)) {
            return "OK";
        }

        return "FAIL";
    }
}