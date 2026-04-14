package cz.stin.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void testHello() {
        hello h = new hello();
        assertEquals("snad uz plne funkci CI/CD pokus cislo 5", h.Hello());
    }

    @Test
    void testHome() {
        hello h = new hello();
        assertEquals("OK podruhe ahaaa", h.home());
    }
}