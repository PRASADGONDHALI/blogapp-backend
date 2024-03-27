package com.example.blog_application.blogapplication.dto;

import com.example.blog_application.blogapplication.payloads.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int id ;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String role;
    private String about ;
    private String password;
    private UserDto userDto;

    public ReqRes() {
    }

    public ReqRes( String error) {
        this.error = error;
    }

   
}
