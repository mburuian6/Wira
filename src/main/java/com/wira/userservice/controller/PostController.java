/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.controller;

import com.wira.userservice.dto.PostDto;
import com.wira.userservice.model.Post;
import com.wira.userservice.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ian
 */
@RestController
@RequestMapping("/api/posts/")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @PostMapping("/post")
    public ResponseEntity createPost(@RequestBody PostDto postdto){
        Post createdPost = postService.createPost(postdto);
        return new ResponseEntity(createdPost,HttpStatus.OK);
    }
    
    @GetMapping("all")
    public ResponseEntity<List<PostDto>> showAllPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return new ResponseEntity<>(postService.showAllPosts(page,size), HttpStatus.OK);
    }
    
    @GetMapping("get/{id}")
    public ResponseEntity<PostDto> getSinglePost(@PathVariable @RequestBody Long id) {
        return new ResponseEntity<>(postService.readSinglePost(id), HttpStatus.OK);
    }
    
    @GetMapping("get/user/{username}")
    public ResponseEntity<List<PostDto>> getPostsOfUser(@PathVariable @RequestBody String username) {
        return new ResponseEntity<>(postService.readPostsByUsername(username), HttpStatus.OK);
    }
    
    @DeleteMapping("delete/{id}")
    public ResponseEntity deletePost(@PathVariable @RequestBody Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
