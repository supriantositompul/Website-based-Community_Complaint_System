package com.puas.clientapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.puas.clientapp.models.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

  @Value("${server.base.url}/category")
  private String baseUrl;

  @Autowired
  private RestTemplate restTemplate;

  public List<Category> getAll() {
    return restTemplate
        .exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Category>>() {
            })
        .getBody();
  }

  public Category create(Category category) {
    return restTemplate
        .exchange(
            "http://localhost:9000/category",
            HttpMethod.POST,
            new HttpEntity<Category>(category),
            new ParameterizedTypeReference<Category>() {
            })
        .getBody();
  }
}
