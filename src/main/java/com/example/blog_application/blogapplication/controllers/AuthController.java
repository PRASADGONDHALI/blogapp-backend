package com.example.blog_application.blogapplication.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.blog_application.blogapplication.dto.ReqRes;
import com.example.blog_application.blogapplication.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@Valid @RequestBody ReqRes signUpRequest ,BindingResult bindingResult){
        ReqRes response = authService.signUp(signUpRequest);
        return new ResponseEntity<ReqRes>(response,HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @GetMapping("/isLoggedIn")
    public ResponseEntity<ReqRes> checkAuthentication() {
        return ResponseEntity.ok(authService.isLoggedIn());
    }
}