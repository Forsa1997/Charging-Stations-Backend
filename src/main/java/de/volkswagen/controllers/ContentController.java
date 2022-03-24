package de.volkswagen.controllers;

import de.volkswagen.models.User;
import de.volkswagen.payload.response.ProfileResponse;
import de.volkswagen.repository.RoleRepository;
import de.volkswagen.repository.UserRepository;
import de.volkswagen.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ContentController {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;

    public ContentController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> hello(@CurrentSecurityContext(expression = "authentication.name")
                                           String username) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        try {
            User currentUser = optionalUser.get();
            ProfileResponse profileResponse = new ProfileResponse(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getRoles());
            return ResponseEntity.ok(profileResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}