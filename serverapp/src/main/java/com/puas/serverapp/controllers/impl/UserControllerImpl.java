package com.puas.serverapp.controllers.impl;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puas.serverapp.controllers.UserController;
import com.puas.serverapp.models.dto.request.UserRequest;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.services.impl.UserServiceImpl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    private UserServiceImpl userServiceImpl;

    @Override
    @GetMapping
    public List<User> getAll() {
        return userServiceImpl.getAll();
    }

    @GetMapping("/{username}")
    public User getByUsername(@PathVariable String username){
        return userServiceImpl.getByUsername(username);
    }

    @Override
    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody UserRequest userRequest) {
        return userServiceImpl.updateUser(username, userRequest);
    }
    
}
