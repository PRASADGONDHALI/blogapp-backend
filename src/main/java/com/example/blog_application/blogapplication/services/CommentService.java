package com.example.blog_application.blogapplication.services;

import com.example.blog_application.blogapplication.payloads.CommentDto;

public interface CommentService {
    
    CommentDto createComment(CommentDto commentDto, Integer postId);

    void deleteComment(Integer commentId);
}
