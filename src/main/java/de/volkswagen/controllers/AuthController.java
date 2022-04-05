package de.volkswagen.controllers;

import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;

import de.volkswagen.payload.request.PasswordChangeRequest;
import de.volkswagen.payload.request.PatchRequest;
import de.volkswagen.payload.response.ProfileResponse;
import de.volkswagen.security.WebSecurityConfig;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import de.volkswagen.models.ERole;
import de.volkswagen.models.Role;
import de.volkswagen.models.User;
import de.volkswagen.payload.request.LoginRequest;
import de.volkswagen.payload.request.SignupRequest;
import de.volkswagen.payload.response.JwtResponse;
import de.volkswagen.payload.response.MessageResponse;
import de.volkswagen.repository.RoleRepository;
import de.volkswagen.repository.UserRepository;
import de.volkswagen.security.jwt.JwtUtils;
import de.volkswagen.security.services.UserDetailsImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully with Roles: " + strRoles));
    }

    @PatchMapping("/patch")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> modifyUser(@Valid @RequestBody PatchRequest patchRequest) {
        Optional<User> optionalUser = userRepository.findById(patchRequest.getId());
        try {
            User currentUser = optionalUser.get();
                currentUser.setFirstName(patchRequest.getFirstName());
                currentUser.setLastName(patchRequest.getLastName());
                currentUser.setEmail(patchRequest.getEmail());
                currentUser.setUsername(patchRequest.getUsername());
            userRepository.save(currentUser);
            ProfileResponse profileResponse = new ProfileResponse(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getRoles());
            return ResponseEntity.ok(profileResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/password")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getId());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try {
            User currentUser = optionalUser.get();
            if (passwordEncoder.matches(request.getOldPassword(),currentUser.getPassword())) {
                currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(currentUser);
                return ResponseEntity.ok("Password succesfully changed");
            } else {
               return ResponseEntity.badRequest().body("Old password did not match");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Password criteria not fullfilled ");
        }
    }

}