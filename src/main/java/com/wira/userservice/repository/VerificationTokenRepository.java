/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.repository;

import com.wira.userservice.model.VerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ian
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    
    Optional<VerificationToken> findByToken(String token);
    
}
