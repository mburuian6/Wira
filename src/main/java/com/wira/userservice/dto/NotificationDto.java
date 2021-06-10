/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.dto;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Ianm
 */

@Data
public class NotificationDto {
    
    private String subject;
    
    private String message;
    
    private String username;
    
    private boolean readStatus;
    
    private LocalDate createdOn;
    
}
