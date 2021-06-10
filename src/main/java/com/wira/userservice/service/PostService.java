/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.service;

import com.wira.userservice.dto.PostDto;
import com.wira.userservice.model.Post;
import com.wira.userservice.repository.PostRepository;
import java.time.Instant;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ian
 */

@Service
public class PostService {
    @Autowired 
    AuthService authService ;
    @Autowired
    PostRepository postRepository; 
    
    public Post createPost(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle().strip().toLowerCase());
        post.setDescription(postDto.getDescription());
        post.setLocation(postDto.getLocation().strip().toLowerCase());        
        post.setStartDate(postDto.getStartDate());
        post.setEndDate(postDto.getEndDate());
        post.setStartTime(postDto.getStartTime());
        post.setHoursPerDay(postDto.getHoursPerDay());
        post.setProposedPay(postDto.getProposedPay());
        post.setTags(postDto.getTags().strip().toLowerCase());
        post.setStatusClosed(true);
        
        User user = authService.getCurrentUser()
                .orElseThrow(()->new IllegalArgumentException("No user logged in"));
        post.setUsername(user.getUsername());
        post.setCreatedOn(Instant.now());
        
        System.out.println("Post Obj: "+ post.getTitle()+ " ;Location - "+post.getLocation()+
                " ;Description - "+post.getDescription()+";Payment - "+ post.getProposedPay()+
                ";Tags - " +post.getTags()+";User - "+ post.getUsername()+" ;starting at "+ post.getStartTime());
        
        Post saved = postRepository.save(post);
        return saved;
    }
    
    public List<PostDto> showAllPosts(int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCreatedOnDesc(pageable);
        return posts.stream().map((post) -> this.mapFromPostToDto(post)).collect(toList());
    }
    
    public PostDto readSinglePost(Long id) {
        return mapFromPostToDto(postRepository.findById(id).get());
    }
    
    public List<PostDto> readPostsByUsername(String username) {
        List<Post> posts = postRepository.findByUsernameOrderByCreatedOnDesc(username);
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }
    
    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setUsername(post.getUsername());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setLocation(post.getLocation());
        postDto.setStartDate(post.getStartDate());
        postDto.setStartDate(post.getStartDate());
        postDto.setEndDate(post.getEndDate());
        postDto.setHoursPerDay(post.getHoursPerDay());
        postDto.setStartTime(post.getStartTime());
        postDto.setTags(post.getTags());
        postDto.setProposedPay(post.getProposedPay());
        postDto.setCreatedOn(post.getCreatedOn().toEpochMilli());
        return postDto;
    }
    
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }
    
    
    
}
