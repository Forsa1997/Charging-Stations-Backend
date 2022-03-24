package de.volkswagen.payload.response;

import de.volkswagen.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfileResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final List<String> roles = new ArrayList<>();

    public ProfileResponse(Long id, String firstName, String lastName, String username, String email, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        roles.forEach(role -> this.roles.add(role.getName().toString()));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}