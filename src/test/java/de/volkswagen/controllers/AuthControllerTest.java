package de.volkswagen.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canSignIn() throws Exception {
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

    @Test
    @WithMockUser
    void canModify() throws Exception {
        mockMvc.perform(patch("/patch").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"firstName\":\"first\",\"lastName\":\"last\",\"username\":\"user3\",\"email\":\"user5@test.de\"}"))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void canChangeThePassword() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user7\",\"password\":\"password\",\"email\":\"user7@test.de\"}"))
                .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user7\",\"password\":\"password\"}"))
                .andExpect(status().isOk()).andReturn();
        String resultString = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String id = jsonParser.parseMap(resultString).get("id").toString();
        mockMvc.perform(patch("/password").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\" " + id+ " \",\"oldPassword\":\"password\",\"newPassword\":\"newpass\"}"))
                .andExpect(status().isOk());
       mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user7\",\"password\":\"newpass\"}"))
                .andExpect(status().isOk());
    }




/*    @Test
    void canModifyForREAL() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"password\",\"email\":\"user1@test.de\"}"))
                .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"password\"}"))
                .andExpect(status().isOk()).andReturn();
        String resultString = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String accessToken = jsonParser.parseMap(resultString).get("accessToken").toString();

        mockMvc.perform(patch("/patch").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3,\"firstName\":\"changed\",\"lastName\":\"last\",\"username\":\"user1\",\"email\":\"email\",\"roles\":[\"ROLE_USER\"],\"accessToken\":\" " + accessToken+ "\",\"tokenType\":\"Bearer\"}"))
                .andExpect(status().isOk());
    }*/



}