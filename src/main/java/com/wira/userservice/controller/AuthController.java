/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.controller;

import com.google.gson.Gson;
import com.wira.userservice.dto.CustomHttpResponse;
import com.wira.userservice.dto.LoginRequest;
import com.wira.userservice.dto.RegisterRequest;
import com.wira.userservice.dto.ResetPasswordTokenResponse;
import com.wira.userservice.dto.UserDto;
import com.wira.userservice.repository.UserRepository;
import com.wira.userservice.service.AuthService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ian
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest){
        UserDto byUsername = authService.getUserByUsername(registerRequest.getUsername());
        if(byUsername == null){
            authService.signup(registerRequest);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }
    
    @PostMapping("/login")
    public ResponseEntity login_(@RequestBody LoginRequest loginRequest){
        System.out.println("LoginRequest Controller. username:"+ loginRequest.getUsername()+"; "+ loginRequest.getPassword());
        
        CustomHttpResponse response = new CustomHttpResponse();
        response.setAuthenticationToken(authService.login(loginRequest));
        response.setUsername(authService.getUsername());
        
        System.out.println("LoginRequest Controller^2. username:"+ loginRequest.getUsername()+"; "+ loginRequest.getPassword());
        
        return new ResponseEntity(new Gson().toJson(response),HttpStatus.OK);
    }
    
    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody UserDto userdto){
        authService.updateUserDetails(userdto);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @GetMapping("/get/{username}")
    public ResponseEntity<UserDto> singleUser(@PathVariable @RequestBody String username){
        return new ResponseEntity<>(authService.getUserByUsername(username), HttpStatus.OK);
    }
    
    //forgot password
    @PostMapping("/forgotpassword")
    public ResponseEntity forgotPassword(@RequestBody UserDto userdto){
        ResetPasswordTokenResponse token = authService.forgotPassword(userdto);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    //Reset password
    @PostMapping("/resetpassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordTokenResponse resetPwd){
        boolean resetPassword = authService.resetPassword(resetPwd.getToken(), resetPwd.getPassword());
        if(resetPassword){
            return new ResponseEntity("Password reset successful!",HttpStatus.OK);
        }
        else{
            return new ResponseEntity("Token is already used. Try again",HttpStatus.FORBIDDEN);
        }
    }
    
    //RANKING
    //update ranking
    @PostMapping("/updaterank/{username}")
    public ResponseEntity updateRank(@PathVariable @RequestBody String username, @PathVariable @RequestBody int newRatingValue){
        try{
            authService.updateRating(newRatingValue, username);
            return new ResponseEntity("Rating Value Updated!",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity("Failed to update Rating!",HttpStatus.FORBIDDEN);
        }
    }
    
    
    
}
