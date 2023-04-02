package com.example.snkrs.repository;

import com.example.snkrs.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ 'username': ?0 }")
    Optional<User> findByUsername(String username);

    @Query("{ 'email': ?0 }")
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Boolean existsByUsername(String username);
}