package de.volkswagen.repository;

import java.util.Optional;

import de.volkswagen.models.Filter;
import de.volkswagen.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
    Optional<User> findByUserId(long userId);
}