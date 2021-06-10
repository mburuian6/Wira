/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.controller;

import com.wira.userservice.dto.BidDto;
import com.wira.userservice.dto.HasBiddedBoolean;
import com.wira.userservice.dto.PostDto;
import com.wira.userservice.model.Bid;
import com.wira.userservice.model.Notification;
import com.wira.userservice.model.Post;
import com.wira.userservice.service.BidService;
import com.wira.userservice.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ian
 */

@RestController
@RequestMapping("/api/bids/")
public class BidController {
    
    @Autowired
    private BidService bidService;
    
    
    @PostMapping("/post")
    public ResponseEntity createBid(@RequestBody BidDto bidDto){
        Bid createdBid = bidService.createBid(bidDto);
        return new ResponseEntity(createdBid,HttpStatus.OK);
    }
    
    @GetMapping("get/{post}")
    public ResponseEntity<List<Bid>> getPostBids(@PathVariable @RequestBody Long post) {
        return new ResponseEntity<>(bidService.readAllByPost(post), HttpStatus.OK);
    }
    
    @GetMapping("get/biddingStatus/{post}/{username}")
    public ResponseEntity<Boolean> getBiddingStatus(@PathVariable @RequestBody Long post, @PathVariable @RequestBody String username){
        System.out.println("Bid Controller: checking bidding status...");    
        boolean checkIfBidded = bidService.checkIfBidded(username, post);
        System.out.println("Bid Controller; has user bidded? "+ checkIfBidded);
        return new ResponseEntity<>(checkIfBidded, HttpStatus.OK);
    }
    
//    @DeleteMapping("delete/{post}")
//    public ResponseEntity deletePostBids(@PathVariable @RequestBody Long id) {
//        bidService.deleteAllByPost(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    
    @PutMapping("/put")
    public ResponseEntity<Notification> updatePostBidToAccepted(@RequestBody Bid bid) {
        Notification notification = bidService.updateBidToAccepted(bid);
        return new ResponseEntity<>(notification,HttpStatus.OK);
    }
    
    
}
