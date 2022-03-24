package de.volkswagen.controllers;

import de.volkswagen.models.ERole;
import de.volkswagen.models.Role;
import de.volkswagen.models.User;
import de.volkswagen.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository localMockRepository;

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void callToUserProfile_OfLoggedInUser_ReturnsUserData() throws Exception {
        User testUser = new User("firstName", "lastName","testuser", "testuser@vw.de", "testuserPW");
        testUser.setId(3L);
        Role moderatorRole = new Role();
        Role userRole = new Role();
        moderatorRole.setName(ERole.ROLE_MODERATOR);
        userRole.setName(ERole.ROLE_USER);
        testUser.setRoles(Stream.of(userRole,moderatorRole)
                .collect(Collectors.toCollection(HashSet::new)));

        when(localMockRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":3,\"firstName\":\"firstName\",\"lastName\":\"lastName\",\"username\":\"testuser\",\"email\":\"testuser@vw.de\",\"roles\":[\"ROLE_USER\",\"ROLE_MODERATOR\"]}"));
    }

    @Test
    void callToUserProfile_WithoutLoggedInUser_ReturnsUnauthorizedError() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isUnauthorized());
    }

}
