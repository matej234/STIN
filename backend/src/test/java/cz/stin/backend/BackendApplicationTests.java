package cz.stin.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"EXCHANGE_API_KEY=dummy"
})
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}
}
