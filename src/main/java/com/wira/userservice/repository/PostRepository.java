/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.repository;

import com.wira.userservice.model.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ian
 */
@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
    
    Post findByTitle(String title);

    Page<Post> findAllByOrderByCreatedOnDesc(Pageable pageable);

    List<Post> findByUsernameOrderByCreatedOnDesc(String username);
}
