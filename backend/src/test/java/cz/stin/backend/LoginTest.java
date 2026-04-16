package cz.stin.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private final Login login = new Login();

    @Test
    void testLoginSuccess() {
        String result = login.login("matej");
        assertEquals("OK", result);
    }

    @Test
    void testLoginEmpty() {
        String result = login.login("");
        assertEquals("FAIL", result);
    }

    @Test
    void testLoginNull() {
        String result = login.login(null);
        assertEquals("FAIL", result);
    }
}