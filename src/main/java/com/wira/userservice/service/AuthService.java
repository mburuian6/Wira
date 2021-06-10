/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.service;

import com.wira.userservice.dto.LoginRequest;
import com.wira.userservice.dto.RegisterRequest;
import com.wira.userservice.dto.ResetPasswordTokenResponse;
import com.wira.userservice.dto.UserDto;
import com.wira.userservice.model.NotificationEmail;
import com.wira.userservice.model.User;
import com.wira.userservice.model.VerificationToken;
import com.wira.userservice.repository.UserRepository;
import com.wira.userservice.repository.VerificationTokenRepository;
import com.wira.userservice.security.JwtProvider;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ian
 */

@Slf4j
@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private MailService mailService;
    
    
    
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setGender(registerRequest.getGender());
        user.setPhone(registerRequest.getPhone());
        user.setJoined(Instant.now());
        user.setRating(0);
        
        System.out.println("register request:"+ user.getUsername()+" :"+user.getPassword());
        User save = userRepository.save(user);
        System.out.println("Saved: "+ save.getUsername());
    }
    
    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
    
    public String login(LoginRequest loginRequest){
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
        
        System.out.println("login request:"+ loginRequest.getUsername()+" :"+loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token =  jwtProvider.generateToken(authenticate);
        System.out.println("JWT-Token:"+ token);
        return token;
    }
    
    public Optional<org.springframework.security.core.userdetails.User > getCurrentUser(){
        org.springframework.security.core.userdetails.User principal = 
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return Optional.of(principal);
    }
    
    public String getUsername(){
        org.springframework.security.core.userdetails.User principal = 
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return principal.getUsername();
    }
    
    public UserDto getUserByUsername(String inputUsername) {
        User user ;

        try {
            user = userRepository.findUserByUsername(inputUsername);
            UserDto userDto = mapFromUserToDto(user);
            return userDto;
        } catch (Exception e) {
            System.out.println("Null User Found:"+ inputUsername+" ;error:"+ e.getMessage());
            return null;
        }
    }
    
    private UserDto mapFromUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());
        userDto.setJoined(user.getJoined());
        return userDto;
    }
    
    public void updateUserDetails(UserDto userDto) {
        User user = userRepository.findUserByUsername(userDto.getUsername());
        
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        
        userRepository.save(user);
    }
    
    public ResetPasswordTokenResponse forgotPassword(UserDto userdto){
        User user = userRepository.findUserByUsername(userdto.getUsername());
        String token = generateVerificationToken(user);
        
        try{
            mailService.sendMail(new NotificationEmail(
                    "Password reset",
                    user.getEmail(), " please click on the below url to reset password for your account "
                        + "and enter : token and new password: " 
                +"http://localhost:4200/reset-password/  .Token: " + token
            ));
        }
        catch(Exception e){ System.out.println("Error: "+ e.getMessage());}
        
        System.out.println("Password reset "+
                user.getEmail()+ " please click on the below url to reset password for your account "
                        + "and enter : token and new password: " 
                +"http://localhost:4200/reset-password/  .Token: " + token);
        //
        
        ResetPasswordTokenResponse tokenObj = new ResetPasswordTokenResponse();
        tokenObj.setToken(token);
        tokenObj.setExpiryDate(Instant.now().plusSeconds(86400));
        return tokenObj;
    }
    
    public boolean resetPassword(String token,String password){
        System.out.println("Token: "+ token);
        System.out.println("Pwd: "+ password);
        
        Optional<VerificationToken> vtokenOpt = verificationTokenRepository.findByToken(token);
        VerificationToken vtoken = vtokenOpt.get();
        //if vtoken is enabled
        if(vtoken.isEnabled()){
            String username = vtoken.getUser();
            System.out.println("User from vtoken: "+ username);

            User userByUsername = userRepository.findUserByUsername(username);
            userByUsername.setPassword(encodePassword(password));
            userRepository.save(userByUsername);

            vtoken.setEnabled(false);
            verificationTokenRepository.save(vtoken);
            return true;
        }
        else{
            return false;
        }
    }
    
    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user.getUsername());
        verificationToken.setExpiryDate(Instant.now().plusSeconds(86400));
        verificationToken.setEnabled(true);
                
        verificationTokenRepository.save(verificationToken);
        return token;
    }
    
    // update rating
    private void updateRating(double rating,String username){
        User findUserByUsername = userRepository.findUserByUsername(username);
        
        
    }
    
    //rating formula - weighted average
    private double rankingFormula(double currentValue,double numberOfRatings, double newValue){
        double total = currentValue * numberOfRatings;
        total += newValue;
        return total/(numberOfRatings+1);
    }
    
    
}
