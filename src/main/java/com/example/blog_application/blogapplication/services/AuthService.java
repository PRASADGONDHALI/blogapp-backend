package com.example.blog_application.blogapplication.services;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import com.example.blog_application.blogapplication.dto.ReqRes;
import com.example.blog_application.blogapplication.exceptions.ResourceNotFoundException;
import com.example.blog_application.blogapplication.models.User;
import com.example.blog_application.blogapplication.payloads.UserDto;
import com.example.blog_application.blogapplication.repository.UserRepo;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService ;

    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            if (registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isEmpty() ) {
                resp.setStatusCode(400);
                resp.setMessage("Email can not be null");
                return resp;
            }
            if (userRepo.existsByEmail(registrationRequest.getEmail())) {
                resp.setStatusCode(400);
                resp.setMessage("Email is already registered");
                return resp;
            }
            if (registrationRequest.getPassword() == null || registrationRequest.getPassword().isEmpty() ) {
                resp.setStatusCode(400);
                resp.setMessage("Email can not be empty");
                return resp;
            }
            if (registrationRequest.getPassword().length() < 8 || registrationRequest.getPassword().length() > 20) {
                resp.setStatusCode(400);
                resp.setMessage("Password must minimum 8 and maximum 20");
                return resp;
            }
            if (!registrationRequest.getPassword().matches(".*\\d.*") ) {
                resp.setStatusCode(400);
                resp.setMessage("Password must contain at least one digit");
                return resp;
            }
            if (!registrationRequest.getPassword().matches(".*[A-Z].*")) {
                resp.setStatusCode(400);
                resp.setMessage("Password must contain at least one uppercase letter");
                return resp;
            }
            if (!registrationRequest.getPassword().matches(".*[a-z].*")) {
                resp.setStatusCode(400);
                resp.setMessage("Password must contain at least one lowercase letter");
                return resp;
            }
            if (!registrationRequest.getPassword().matches(".*[!@#$%&*()-+=^].*")) {
                resp.setStatusCode(400);
                resp.setMessage("Password must contain at least one special character");
                return resp;
            }
            if (registrationRequest.getPassword().contains(" ")) {
                resp.setStatusCode(400);
                resp.setMessage("Password should not consist whitespace");
                return resp;
            }
            if (registrationRequest.getAbout() == null || registrationRequest.getAbout().isEmpty() ) {
                resp.setStatusCode(400);
                resp.setMessage("Email can not be null");
                return resp;
            }
            User ourUsers = new User();
            ourUsers.setName(registrationRequest.getName());
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole("USER");
            ourUsers.setAbout(registrationRequest.getAbout());
            User ourUserResult = userRepo.save(ourUsers);
            if (ourUserResult != null && ourUserResult.getId()>0) {
                UserDto userDto = new UserDto();
                userDto.setId(ourUsers.getId());
                userDto.setName(ourUsers.getName());
                userDto.setEmail(ourUsers.getEmail());
                userDto.setAbout(ourUserResult.getAbout());
                userDto.setPassword(ourUserResult.getPassword());
                resp.setUserDto(userDto);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            if (!userRepo.existsByEmail(signinRequest.getEmail())) {
                response.setStatusCode(400);
                response.setMessage("Email is not registered");
                return response;
            }
            try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            }catch(Exception e){
                response.setStatusCode(400);
                response.setMessage("Invalid Password");
                return response;   
            }
            var user = userRepo.findByEmail(signinRequest.getEmail()).orElseThrow(()->new ResourceNotFoundException("User", "Email"+signinRequest.getEmail(), 0));
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(signinRequest.getEmail());
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            
            User user1 = (User) userDetails;
            UserDto userDto = new UserDto();
            userDto.setId(user1.getId());
            userDto.setName(user1.getName());
            userDto.setEmail(user1.getEmail());
            userDto.setAbout(user1.getAbout());
            userDto.setPassword(user1.getPassword());
            
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
            response.setUserDto(userDto);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        User users = userRepo.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }

    // check user logged in or not
    public ReqRes isLoggedIn(){
        ReqRes response = new ReqRes();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(authentication.getName());
            User user1 = (User) userDetails;
            UserDto userDto = new UserDto();
            userDto.setId(user1.getId());
            userDto.setName(user1.getName());
            userDto.setEmail(user1.getEmail());
            userDto.setAbout(user1.getAbout());
            userDto.setPassword(user1.getPassword());
            response.setStatusCode(200);
            response.setMessage("User is logged in");
            response.setUserDto(userDto);
        } else {
            response.setStatusCode(500);
            response.setMessage("Unauthorized request");
        }
        return response ;
    }
}
