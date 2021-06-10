/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wira.userservice.exception;

/**
 *
 * @author Ian
 */
public class WiraException extends RuntimeException{
    
    public WiraException(String message){
        super(message);
    }
    
}
