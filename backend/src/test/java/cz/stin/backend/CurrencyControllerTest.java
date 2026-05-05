package cz.stin.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "EXCHANGE_API_KEY=dummy"
})
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rates_endpoint_works() throws Exception {
        mockMvc.perform(get("/api/currency/rates"))
                .andExpect(status().isOk());
    }

    @Test
    void timeframe_endpoint_works() throws Exception {
        mockMvc.perform(get("/api/currency/timeframe")
                        .param("start_date", "2024-01-01")
                        .param("end_date", "2024-01-10"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailWithoutParams() throws Exception {
        mockMvc.perform(get("/api/currency/timeframe"))
                .andExpect(status().is4xxClientError());
    }
}
