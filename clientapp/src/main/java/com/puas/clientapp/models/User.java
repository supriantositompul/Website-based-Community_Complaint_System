package com.puas.clientapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;
    
    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private String username;

    private String password;

}