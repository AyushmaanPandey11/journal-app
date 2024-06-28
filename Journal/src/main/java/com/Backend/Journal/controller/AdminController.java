package com.Backend.Journal.controller;

import com.Backend.Journal.entity.User;
import com.Backend.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.getAll();
        if(allUsers != null && !allUsers.isEmpty()){
            return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(User user){
        User admin = userService.saveAdmin(user);
        if(admin!=null){
            return new ResponseEntity<>(admin,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
