package cz.stin.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "currency.provider=mock"
})
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void analyze_works() throws Exception {
        mockMvc.perform(get("/api/currency/analyze")
                        .param("base", "EUR")
                        .param("currencies", "USD,CZK"))
                .andExpect(status().isOk());
    }

    @Test
    void currencies_works() throws Exception {
        mockMvc.perform(get("/api/currency/currencies"))
                .andExpect(status().isOk());
    }

    @Test
    void timeframe_works() throws Exception {
        mockMvc.perform(get("/api/currency/timeframe")
                        .param("base", "EUR")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-10"))
                .andExpect(status().isOk());
    }

    @Test
    void timeframe_invalid_date_order_should_fail() throws Exception {
        mockMvc.perform(get("/api/currency/timeframe")
                        .param("base", "EUR")
                        .param("startDate", "2024-01-10")
                        .param("endDate", "2024-01-01")
                        .param("currencies", "USD,CZK"))
                .andExpect(status().isBadRequest());
    }
}