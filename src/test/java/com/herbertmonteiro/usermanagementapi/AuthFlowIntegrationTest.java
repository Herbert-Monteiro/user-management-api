package com.herbertmonteiro.usermanagementapi;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Rota protegida sem token é barrada")
    void shouldRejectBooksWithoutToken() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Registra, loga e acessa rota protegida com o token")
    void shouldRegisterLoginAndListBooks() throws Exception {
        String register = """
                {"name":"Teste","cpf":"12345678901","email":"teste@teste.com",
                 "password":"123456","birthDate":"2000-01-01"}
                """;
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        String login = """
                {"email":"teste@teste.com","password":"123456"}
                """;
        String body = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = JsonPath.read(body, "$.token");

        mockMvc.perform(get("/books")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
