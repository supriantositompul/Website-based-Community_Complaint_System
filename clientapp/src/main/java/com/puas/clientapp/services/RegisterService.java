package com.puas.clientapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.puas.clientapp.models.dto.request.RegisterRequest;
import com.puas.clientapp.models.dto.response.RegisterResponse;

@Service
public class RegisterService {

    @Value("${server.base.url}/auth/registration")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public boolean register(RegisterRequest registerRequest) {

        // Buat HttpEntity dengan body registerRequest
        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(registerRequest);

        // Kirim HTTP POST request dengan RestTemplate
        ResponseEntity<RegisterResponse> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                requestEntity,
                RegisterResponse.class);

        // Periksa status code untuk menentukan apakah registrasi berhasil
        return responseEntity.getStatusCode().is2xxSuccessful();
    }
}
