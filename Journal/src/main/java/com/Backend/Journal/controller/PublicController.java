package com.Backend.Journal.controller;


import com.Backend.Journal.controller.entity.User;
import com.Backend.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("health-check")
    public String healthCheck(){
        return "Working Properly";
    }

    @PostMapping("create-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            User createdUser = userService.saveEntry(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}