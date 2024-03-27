package com.example.blog_application.blogapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_application.blogapplication.models.User;

public interface UserRepo extends JpaRepository<User,Integer>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
