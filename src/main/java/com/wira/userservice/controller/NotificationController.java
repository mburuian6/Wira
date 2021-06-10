/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.controller;

import com.wira.userservice.model.Bid;
import com.wira.userservice.model.Notification;
import com.wira.userservice.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ianm
 */

@RestController
@RequestMapping("/api/notifications/")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("get/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return new ResponseEntity<>(notificationService.readAll(), HttpStatus.OK);
    }
    
    @GetMapping("get/today")
    public ResponseEntity<List<Notification>> getTodayNotifications() {
        return new ResponseEntity<>(notificationService.readAllToday(), HttpStatus.OK);
    }
    
}
