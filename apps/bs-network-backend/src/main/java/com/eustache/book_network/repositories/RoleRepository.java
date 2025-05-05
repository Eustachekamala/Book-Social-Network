package com.eustache.book_network.repositories;

import com.eustache.book_network.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(String role);
}
