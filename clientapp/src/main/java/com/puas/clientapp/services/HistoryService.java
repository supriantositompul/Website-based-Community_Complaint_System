package com.puas.clientapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.puas.clientapp.models.History;
import com.puas.clientapp.models.dto.response.ApiResponse;

@Service
public class HistoryService {

    @Value("${server.base.url}/history")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<History> getAllHistoryByUser() {
        String url = baseUrl;
        ResponseEntity<ApiResponse<List<History>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<List<History>>>(){});
        return response.getBody().getData();
    }

    public History getByIdWithDTO(Integer id) {
        String url = baseUrl + "/" + id;
        ResponseEntity<ApiResponse<History>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<History>>(){});
        return response.getBody().getData();
    }
}
