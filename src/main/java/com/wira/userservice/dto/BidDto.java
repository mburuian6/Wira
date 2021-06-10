/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.dto;

/**
 *
 * @author Ianm
 */

import java.time.Instant;
import lombok.Data;

@Data
public class BidDto {
    
    private Long post;
    private String username;
    boolean statusAccepted;
    private Instant createdOn;
    private double payment;
    
}
