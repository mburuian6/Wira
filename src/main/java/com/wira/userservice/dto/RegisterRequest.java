/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.dto;

import java.time.Instant;
import lombok.Data;

/**
 *
 * @author Ian
 */

@Data
public class RegisterRequest {
    private String username;
    
    private String password;
    
    private String email;
    
    private String name;
    
    private String phone;
    
    private String gender;
    
    private Instant joined;
}
