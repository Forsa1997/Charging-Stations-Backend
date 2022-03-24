package de.volkswagen.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canSingIn() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"password\",\"email\":\"user1@test.de\"}"))
                        .andExpect(status().isOk());
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"password\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    void canSignUp() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user2\",\"password\":\"password\",\"email\":\"user3@test.de\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void adminCanAccessAdminContent() throws Exception {
        mockMvc.perform(get("/api/test/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void userCanNotAccessAdminAndModContent() throws Exception {
        mockMvc.perform(get("/api/test/admin")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/test/mod")).andExpect(status().isForbidden());
    }

    @Test
    void canRegisterAnAdmin() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin1\",\"password\":\"password\",\"email\":\"admin@admin.de\",\"role\":[\"ROLE_ADMIN\"]}"))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User registered successfully with Roles: [ROLE_ADMIN]\"}"));

    }
}