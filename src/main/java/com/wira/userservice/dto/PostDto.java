/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.dto;

import com.wira.userservice.model.User;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 *
 * @author Ian
 */
@Data
public class PostDto {
    
    private Long id ;
    private String username;
    private String title;
    private String description;
    private String location;
    private Date startDate;
    private Date endDate;
    private String hoursPerDay;
    private String startTime; 
    private Double proposedPay;
    private String tags;
    private Long createdOn;
    private boolean statusClosed;
}
