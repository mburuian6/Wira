/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.service;

import com.wira.userservice.dto.BidDto;
import com.wira.userservice.model.Bid;
import com.wira.userservice.model.Notification;
import com.wira.userservice.model.Post;
import com.wira.userservice.repository.BidRepository;
import com.wira.userservice.repository.NotificationRepository;
import com.wira.userservice.repository.PostRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ian
 */
@Service
public class BidService {
    
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    BidRepository bidRepository;
    
    @Autowired 
    AuthService authService ;
    
    @Autowired 
    NotificationService notificationService ;
    
    Notification notification;
    
    //create
    public Bid createBid(BidDto requestbid){
        Bid bid = new Bid();
        User user = authService.getCurrentUser()
                .orElseThrow(()->new IllegalArgumentException("No user logged in"));
        
        bid.setProposedPayment(requestbid.getPayment());
        bid.setUsername(user.getUsername());
        bid.setStatusAccepted(false);
        bid.setCreatedOn(Instant.now());
        
        System.out.println("Saving bid: from "+bid.getUsername()+
                "; created on "+ bid.getCreatedOn()+
                "; for the post "+ bid.getPost() +
                "; priced at "+ bid.getProposedPayment()
        );
        return bidRepository.save(bid);
    }
    
    //read for particular post
    public List<Bid> readAllByPost(Long post){
        System.out.println("Reading bids for post: "+ post);
        return bidRepository.findByPostOrderByCreatedOnDesc(post);
    }
    
    //check if user has "bidded"
    public boolean checkIfBidded(String username,Long post){
        System.out.println("Checking if bidded; Reading bids for post: "+ post);
        List<Bid> postBids = bidRepository.findByPostOrderByCreatedOnDesc(post);
//        Iterator<Bid> iterator = postBids.iterator();
//        while(iterator.hasNext()){
//            if(iterator.next().getUsername().equalsIgnoreCase(username)){ 
//                System.out.println("User has bidded");
//                return true; 
//            }
//        }

        return postBids.stream()
                .anyMatch(n-> n.getUsername().equalsIgnoreCase(username));
//        System.out.println("User has not bidded");
//        return false;        
    }
    
    //delete
    public boolean deleteAllByPost(Long post){
        try{
            List<Bid> findByPost= bidRepository.findByPostOrderByCreatedOnDesc(post);
            Iterator<Bid> iterator = findByPost.iterator();
            
            while(iterator.hasNext()){
                Bid next = iterator.next();
                if(!next.isStatusAccepted()){
                    bidRepository.delete(next);
                }
            }
            
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    //update
    public Notification updateBidToAccepted(Bid bid){
        //update bid
//        Bid bid = bidRepository.findById(bidId).get();
        bid.setStatusAccepted(true);
        bidRepository.save(bid);
        //update post
        Post post = postRepository.findById(bid.getPost()).get();
        post.setStatusClosed(true);
        postRepository.save(post);
        
        //create and return notification
        Notification notification = createNotification(bid);
        
        //delete all other bids
        //new thread
        Thread thread = new Thread(){
            @Override
            public void run(){
                deleteAllByPost(post.getId());
            }
        }; thread.start();
        
        return notification;
    }
    
    
    public Notification createNotification(Bid bid){
        Post post = postRepository.findById(bid.getPost()).get();
        //create notification for bid owner
        String subject = "bid accepted!";
        String message = "your bid costing:"+ bid.getProposedPayment()+
                " has been accepted for post: [link] "+ post.getId()+
                " . Owner is  [link]" + post.getUsername()+" for further arrangements";
        notificationService.createNotification(subject, message,bid.getUsername());
        
        //create notification for post owner
        subject = "bid accepted!";
        message = "the bid id: "+ bid.getId()+ " from " + bid.getUsername()+
                " has been accepted for post: [link] "+ post.getId()+
                " . Owner is  [link]" + post.getUsername()+" for further arrangements";
        
        
        return notificationService.createNotification(subject, message);
    }
    
}
