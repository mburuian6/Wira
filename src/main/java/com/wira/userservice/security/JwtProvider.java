/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.security;

import com.wira.userservice.exception.WiraException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ian
 */
@Service
public class JwtProvider {
    private KeyStore keyStore;
    
    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
            
        }
        catch(IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e){
            e.printStackTrace();
        }
    }
    
    public String generateToken(Authentication authentication){
        User principal = (User)authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }
    
    public Key getPrivateKey(){
        String alias = "springblog";
        String password = "secret";
        try{
            return (PrivateKey)keyStore.getKey(alias, password.toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new WiraException("Exception occurred while retrieving private key from keystore");
        }
    }
    
    public boolean validateToken(String jwt){
        Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt);
        return true;
    }
    
    public PublicKey getPublicKey(){
        String alias = "springblog";
        String password = "secret";
        try{
            return keyStore.getCertificate(alias)
                    .getPublicKey();
        }
        catch(KeyStoreException e){
            throw new WiraException("Exception occurred while retrieving public key from keystore");
        }
    }
    
    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    
}
