/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.repository;

import com.wira.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Ian
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
    User findUserByUsername(String username);
    
    
    
}
