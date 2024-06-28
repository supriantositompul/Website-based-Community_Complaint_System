package com.puas.serverapp.controllers;

import java.util.List;

import com.puas.serverapp.models.dto.request.UserRequest;
import com.puas.serverapp.models.entities.User;

public interface UserController {
    List<User> getAll();

    User updateUser(String username, UserRequest userRequest);
}
