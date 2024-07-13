package com.Backend.Journal.controller;


import com.Backend.Journal.entity.User;
import com.Backend.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("health-check")
    public String healthCheck(){
        return "Working Properly";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            User createdUser = userService.saveNewUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
