package com.example.blog_application.blogapplication.services;

import java.util.List;

import com.example.blog_application.blogapplication.payloads.CategoryDto;

public interface CategoryService {
    
    public CategoryDto createCategory(CategoryDto categoryDto) ;

    public CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);

    public void deleteCategory(Integer categoryId) ;
    
    public CategoryDto getCategory(Integer categoryId) ;

    public List<CategoryDto> getCategories() ;
}
