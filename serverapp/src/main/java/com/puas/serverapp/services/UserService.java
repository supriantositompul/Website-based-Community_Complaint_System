package com.puas.serverapp.services;

import java.util.List;

import com.puas.serverapp.models.dto.request.UserRequest;
import com.puas.serverapp.models.entities.User;

public interface UserService {

    List<User> getAll();

    User updateUser(String username, UserRequest userRequest);
}
