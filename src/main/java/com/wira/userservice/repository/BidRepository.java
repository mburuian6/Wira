/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.repository;

import com.wira.userservice.model.Bid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ian
 */
public interface BidRepository extends JpaRepository<Bid,Long>{

    public List<Bid> findByPostOrderByCreatedOnDesc(Long post);

    public void deleteByPost(Long post);
    
}
