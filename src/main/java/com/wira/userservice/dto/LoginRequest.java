/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.dto;

import lombok.Data;

/**
 *
 * @author Ian
 */

@Data
public class LoginRequest {
    private String username;
    private String password;
}
