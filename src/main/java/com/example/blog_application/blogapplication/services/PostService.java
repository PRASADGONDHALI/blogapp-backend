package com.example.blog_application.blogapplication.services;

import java.util.List;

import com.example.blog_application.blogapplication.dto.ReqRes;
import com.example.blog_application.blogapplication.payloads.PostDto;
import com.example.blog_application.blogapplication.payloads.PostResponse;

public interface PostService {
    
    // create
    ReqRes createPost(PostDto postDto,Integer userId, Integer categoryId);

    // update 
    PostDto updatePost(PostDto postDto, Integer postId);

    // delete
    void deletePost(Integer postId);

    // get all posts 
    PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) ;

    // get single post
    PostDto getPostById(Integer postId) ;

    // get all post by category
    List<PostDto> getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    // get all posts by users
    List<PostDto> getPostsByUser(Integer userId) ;

    // search post 
    List<PostDto> searchPosts(String keywords) ;
}
