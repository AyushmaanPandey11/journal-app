package com.Backend.Journal.controller;


import com.Backend.Journal.entity.User;
import com.Backend.Journal.service.UserDetailServiceImpl;
import com.Backend.Journal.service.UserService;
import com.Backend.Journal.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("health-check")
    public String healthCheck(){
        return "Working Properly";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            User createdUser = userService.saveNewUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailService.loadUserByUsername(user.getUserName());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return  new ResponseEntity<>(token,HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception while createAuthenticationToken ",e);
            return new ResponseEntity<>("Incorrect Credentials",HttpStatus.BAD_REQUEST);
        }
    }
}
