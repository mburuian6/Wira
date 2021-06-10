/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.repository;

import com.wira.userservice.model.Notification;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ianm
 */
public interface NotificationRepository extends JpaRepository<Notification,Long>{

    List<Notification> findByUsernameOrderByCreatedOnDesc(String username);

    public List<Notification> findByUsernameAndCreatedOnOrderByCreatedOnDesc(String username,LocalDate date);

    public List<Notification> findByUsernameAndReadStatusOrderByCreatedOnDesc(String username,boolean readStatus);
    
}
