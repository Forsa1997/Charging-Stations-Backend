package de.volkswagen.controllers;

import de.volkswagen.models.Filter;
import de.volkswagen.models.User;
import de.volkswagen.payload.request.FilterRequest;
import de.volkswagen.payload.response.FilterResponse;
import de.volkswagen.payload.response.MessageResponse;
import de.volkswagen.repository.FilterRepository;
import de.volkswagen.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


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
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> saveFilter(@Valid @RequestBody FilterRequest filterRequest) {
        Long userId = filterRequest.getUserId();
        if (userRepository.existsById(userId)) {
            User user = userRepository.getById(userId);
            for (Filter filter:user.getFilters()){
                if (filter.getName().equals(filterRequest.getName())){
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Filtername already exists"));
                }
            }
            Filter newFilter = new Filter(filterRequest.getName(),filterRequest.getFilterKw(), filterRequest.getFilterPlugtype(), filterRequest.getFilterOperator(), filterRequest.getFilterFreeToUse(), user);
            filterRepository.save(newFilter);

            /*Set<Filter> filterSet = user.getFilters();
            filterSet.add(newFilter);
            user.setFilters(filterSet);
            userRepository.save(user);*/
            return ResponseEntity.ok(new MessageResponse("Filter succsessfully safed!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: UserId does not exist!"));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
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

    @DeleteMapping("/filter")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteFilter(@Valid @RequestBody FilterRequest filterRequest, @CurrentSecurityContext(expression = "authentication.name")
            String username) {

        User currentUser = userRepository.findByUsername(username).get();
        List<Filter> toDelete = currentUser.getFilters().stream().filter(filter -> filter.getName().equals(filterRequest.getName())).collect(Collectors.toList());
        if (toDelete.size() > 0){
            filterRepository.deleteAll(toDelete);
            return ResponseEntity.ok().body("Filter has been deleted!");
        } else {
            return ResponseEntity.badRequest().body("No such filter found!");
        }
    }







}

