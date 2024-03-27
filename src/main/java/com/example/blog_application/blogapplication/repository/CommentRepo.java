package com.example.blog_application.blogapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_application.blogapplication.models.Comment;

public interface CommentRepo extends JpaRepository<Comment,Integer> {
    
}
