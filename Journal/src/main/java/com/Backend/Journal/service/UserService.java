package com.Backend.Journal.service;


import com.Backend.Journal.entity.User;
import com.Backend.Journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User saveNewUser(@org.jetbrains.annotations.NotNull User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
        return user;
    }
    public User saveAdmin(@org.jetbrains.annotations.NotNull User user){
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUserName(user.getUserName()));
        if(existingUser.isPresent()){
            User userToUpdate = existingUser.get();
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            userToUpdate.setRoles(List.of("USER","ADMIN"));
            userRepository.save(userToUpdate);
            return userToUpdate;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER","ADMIN"));
            userRepository.save(user);
            return user;
        }
    }

    public List<User> getAll(){

        return userRepository.findAll();
    }
    public Optional<User> findById(ObjectId id){

        return userRepository.findById(id);
    }
    public void deleteById(ObjectId id){

        userRepository.deleteById(id);
    }
    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
