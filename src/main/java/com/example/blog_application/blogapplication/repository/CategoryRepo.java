package com.example.blog_application.blogapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_application.blogapplication.models.Category;

public interface CategoryRepo extends JpaRepository<Category,Integer>{
    

}
