package de.volkswagen.repository;

import java.util.Optional;

import de.volkswagen.models.ERole;
import de.volkswagen.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}