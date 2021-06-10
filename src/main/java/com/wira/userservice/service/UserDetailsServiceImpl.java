/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.service;

import com.wira.userservice.model.User;
import com.wira.userservice.repository.UserRepository;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ian
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException{
        
        User user;
        
        try{
            user = userRepository.findUserByUsername(username);
        }
        catch(Exception e){
            throw new UsernameNotFoundException("User not found: "+ username);
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,true,true,true,
                getAuthorities("ROLE_USER"));
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(String role_user){
        return Collections.singletonList(new SimpleGrantedAuthority(role_user));
    }
    
}
