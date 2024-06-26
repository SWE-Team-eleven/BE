package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
