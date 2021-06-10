/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.service;

import com.wira.userservice.model.Notification;
import com.wira.userservice.repository.NotificationRepository;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ianm
 */

@Service
public class NotificationService {
    
    @Autowired 
    NotificationRepository notificationRepository ;
    
    @Autowired 
    AuthService authService ;
    
    User user;
    
    public Notification createNotification(String subject,String message){
        Notification notification = new Notification();
        
        if (user==null){
            user = returnUser();
        }
        
        notification.setSubject(subject);
        notification.setUsername(user.getUsername());
        notification.setReadStatus(false);
        notification.setCreatedOn(LocalDate.now());
        notification.setMessage(message);
        
        Notification saved = notificationRepository.save(notification);
        return saved;
    }
    
    public Notification createNotification(String subject,String message,String username){
        Notification notification = new Notification();
        
        notification.setSubject(subject);
        notification.setUsername(username);
        notification.setReadStatus(false);
        notification.setCreatedOn(LocalDate.now());
        notification.setMessage(message);
        
        Notification saved = notificationRepository.save(notification);
        return saved;
    }
    
    private User returnUser(){
        if (user==null){
            user = authService.getCurrentUser()
                .orElseThrow(()->new IllegalArgumentException("No user logged in"));
        }
        return user;
    }
    
    public List<Notification> readAllByReadStatus(boolean readStatus){
        if (user==null){
            user = returnUser();
        }
        return notificationRepository.findByUsernameAndReadStatusOrderByCreatedOnDesc(user.getUsername(),readStatus);
    }
    
    
    public List<Notification> readAll(){
        if (user==null){
            user = returnUser();
        }
        
        //get read and unread separate
        List<Notification> readNotifications = readAllByReadStatus(true);
        List<Notification> unreadNotifications = readAllByReadStatus(false);
        
        //join them with read at the bottom
        unreadNotifications.addAll(readNotifications);
        return unreadNotifications;
    }
    
    public List<Notification> readAllToday(){
        Calendar cal = new GregorianCalendar();
        Integer day = cal.get(Calendar.DATE);
        Integer month = cal.get(Calendar.MONTH)+1;
        Integer year = cal.get(Calendar.YEAR);
        
        String dateStr = day.toString()+"-"+month.toString()+"-"+year.toString();
        LocalDate date = LocalDate.parse(dateStr);
        
        if (user==null){
            user = returnUser();
        }
        return notificationRepository.findByUsernameAndCreatedOnOrderByCreatedOnDesc(user.getUsername(),date);
    }
    
}
