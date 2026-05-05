package cz.stin.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_ok() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Matěj\",\"password\":\"MATEJ\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void login_wrong_password() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Matěj\",\"password\":\"wrong\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAIL"));
    }

    @Test
    void login_wrong_username() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"X\",\"password\":\"MATEJ\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAIL"));
    }

    @Test
    void login_empty() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAIL"));
    }
    @Test
    void login_null_username() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"MATEJ\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAIL"));
    }

    @Test
    void login_null_password() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Matěj\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAIL"));
    }
}