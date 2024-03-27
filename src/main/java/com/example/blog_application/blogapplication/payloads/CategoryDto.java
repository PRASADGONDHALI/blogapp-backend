package com.example.blog_application.blogapplication.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryDto {
    
    private Integer categoryId ;
    @NotEmpty
    @Size(min = 4,message = "minimum size is 4")
    private String categoryTitle ;
    @NotEmpty
    @Size(min = 10,message = "minimum size is 10")
    private String categoryDescription ;
}
