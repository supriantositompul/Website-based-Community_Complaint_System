package com.puas.serverapp.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String username;
    private String password;
}
