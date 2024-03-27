package com.example.blog_application.blogapplication.payloads;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {

    private int id ;
    
    private String name ;
    private String email;
    private String password ;
    private String about ;

    @JsonIgnore
    public String getPassword(){
        return this.password ;
    }
    
}
