package de.volkswagen.controllers;

import de.volkswagen.models.ERole;
import de.volkswagen.models.Filter;
import de.volkswagen.models.Role;
import de.volkswagen.models.User;
import de.volkswagen.payload.request.FilterRequest;
import de.volkswagen.payload.response.FilterResponse;
import de.volkswagen.payload.response.MessageResponse;
import de.volkswagen.payload.response.ProfileResponse;
import de.volkswagen.repository.FilterRepository;
import de.volkswagen.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FilterController {

    FilterRepository filterRepository;
    UserRepository userRepository;

    public FilterController(FilterRepository filterRepository, UserRepository userRepository) {
        this.filterRepository = filterRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/filter")
    public ResponseEntity<?> saveFilter(@Valid @RequestBody FilterRequest filterRequest) {
        Long userId = filterRequest.getUserId();
        if (userRepository.existsById(userId)) {
            User user = userRepository.getById(userId);

            for (Filter filter:user.getFilters()){
                if (filter.getName().equals(filterRequest.getName())){
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                } else {
                    return ResponseEntity.ok(new MessageResponse("Filter succsessfully safed!"));
                }
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: UserId does not exist!"));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> loadFilter(@CurrentSecurityContext(expression = "authentication.name")
                                                    String username) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        try {
            User currentUser = optionalUser.get();
            FilterResponse filterResponse = new FilterResponse(currentUser.getFilters());
            return ResponseEntity.ok(filterResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("User does not exist!");
        }
    }







}

