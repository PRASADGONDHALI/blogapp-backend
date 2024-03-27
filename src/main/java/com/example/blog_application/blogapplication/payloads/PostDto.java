package com.example.blog_application.blogapplication.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDto {

    private Integer postId;
    
    private String title ;

    private String content ;

    private String imageName ;

    private Date addedDate ;

    private CategoryDto category ;

    private UserDto user ;

    private Set<CommentDto> comments = new HashSet<>();
}
