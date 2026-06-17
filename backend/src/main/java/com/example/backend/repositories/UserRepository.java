package com.example.backend.repositories;

import com.example.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByAccount_Id(Long accountId);
    UserEntity findByUsername(String username);
    Boolean existsByUsername(String username);
}