package com.example.blog_application.blogapplication.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.blog_application.blogapplication.models.Category;
import com.example.blog_application.blogapplication.models.Post;
import com.example.blog_application.blogapplication.models.User;

public interface PostRepo extends JpaRepository<Post,Integer>{
    
    List<Post> findByUser(User user);
    Page<Post> findByCategory(Category category, Pageable pageable);
    @Query("select p from Post p where p.title like :key")
    List<Post> searchByTitle(@Param("key") String title);
}
