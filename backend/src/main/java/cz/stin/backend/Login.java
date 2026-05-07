package cz.stin.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Login {

    private final String USERNAME = "Matěj";
    private final String PASSWORD_HASH =
            BCrypt.hashpw("MATEJ", BCrypt.gensalt());

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {

        if (req == null ||
                req.username == null || req.username.isBlank() ||
                req.password == null || req.password.isBlank()) {
            return new LoginResponse(false, "Nezadané kompletní přihlašovací údaje");
        }

        boolean userOk = req.username.equals(USERNAME);
        boolean passOk = BCrypt.checkpw(req.password, PASSWORD_HASH);

        if (userOk && passOk) {
            return new LoginResponse(true, "OK");
        }

        return new LoginResponse(false, "Neplatné přihlašovací údaje");
    }
}