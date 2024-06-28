package com.puas.clientapp.services;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.puas.clientapp.models.Status;


@Service
@AllArgsConstructor
public class StatusService {

  private RestTemplate restTemplate;

  public List<Status> getAll() {
    return restTemplate
      .exchange(
        "http://localhost:9000/Status",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<Status>>() {}
      )
      .getBody();
  }

  public Status create(Status status) {
    return restTemplate
      .exchange(
        "http://localhost:9000/Status",
        HttpMethod.POST,
        new HttpEntity<Status>(status),
        new ParameterizedTypeReference<Status>() {}
      )
      .getBody();
  }
}
