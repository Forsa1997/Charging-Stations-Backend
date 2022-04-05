package de.volkswagen.controllers;

import de.volkswagen.models.Filter;
import de.volkswagen.models.User;
import de.volkswagen.repository.FilterRepository;
import de.volkswagen.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class FilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

   /* @MockBean
    UserRepository localMockRepository;*/



    @Test
    @WithMockUser
    void ReturnsErrorWhenFilterNameIsAlreadyUsed() throws Exception {
        mockMvc.perform(post("/filter").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"mock\",\"userId\":\"1\",\"filterFreeToUse\":[\"yes\"],\"filterOperator\":[\"36,180,3463\"]}"))
                        .andExpect(status().isOk());
        mockMvc.perform(post("/filter").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"mock\",\"userId\":\"1\",\"filterFreeToUse\":[\"yes\"],\"filterOperator\":[\"36,180,3463\"]}"))
                         .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void canSaveFilter() throws Exception {
        mockMvc.perform(post("/filter").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"myFilter\",\"userId\":\"1\",\"filterFreeToUse\":[\"yes\"],\"filterOperator\":[\"36,180,3463\"]}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void canLoadFilter() throws Exception {
        mockMvc.perform(post("/filter").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"mocktest\",\"userId\":\"2\",\"filterFreeToUse\":[\"yes\"],\"filterOperator\":[\"36,180,3463\"]}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/filter")).andExpect(status().isOk()).andDo(print());
    }
}
