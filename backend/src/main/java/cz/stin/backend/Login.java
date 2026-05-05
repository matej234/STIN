package cz.stin.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Login {

    private final String HASHED_PASSWORD =
            BCrypt.hashpw("MATEJ", BCrypt.gensalt());

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {

        if (req == null || req.username == null || req.password == null) {
            return new LoginResponse("FAIL", null);
        }

        if (!"Matěj".equals(req.username)) {
            return new LoginResponse("FAIL", null);
        }

        if (BCrypt.checkpw(req.password, HASHED_PASSWORD)) {
            return new LoginResponse("OK", req.username);
        }

        return new LoginResponse("FAIL", null);
    }
}