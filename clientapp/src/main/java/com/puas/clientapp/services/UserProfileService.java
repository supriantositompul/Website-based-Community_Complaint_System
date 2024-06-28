package com.puas.clientapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.puas.clientapp.models.User;

@Service
public class UserProfileService {

    @Value("${server.base.url}/user")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public User getUserByUsername(String username) {
        String url = baseUrl + "/" + username;
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, null, User.class);
        return response.getBody();
    }

    public User updateUser(String username, User user) {
        String url = baseUrl +  "/" + username;
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<User>(user),
                User.class);
        return response.getBody();
    }

}

// public void updateEmployee(String username, ProfileEmployee employeeRequest)
// {
// String url = baseUrl + "/update/" + username;
// restTemplate
// .exchange(url, HttpMethod.PUT, new
// HttpEntity<ProfileEmployee>(employeeRequest), ProfileEmployee.class)
// .getBody();
// }