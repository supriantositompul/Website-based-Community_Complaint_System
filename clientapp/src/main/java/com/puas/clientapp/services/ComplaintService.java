package com.puas.clientapp.services;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.puas.clientapp.models.Attachment;
import com.puas.clientapp.models.Complaint;
import com.puas.clientapp.models.dto.request.ComplaintRequest;

@Service
public class ComplaintService {

    @Value("${server.base.url}/complaint")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Complaint> getAll() {
        return restTemplate.exchange(baseUrl, HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Complaint>>() {
                }).getBody();
    }

    public Complaint getById(Integer id) {
        String url = baseUrl + "/" + id;
        return restTemplate.getForObject(url, Complaint.class);
    }

    // public Complaint create(ComplaintRequest complaint) {
    // return restTemplate.exchange(baseUrl, HttpMethod.POST,
    // new HttpEntity<ComplaintRequest>(complaint),
    // new ParameterizedTypeReference<Complaint>() {
    // }).getBody();
    // }

    public Complaint create(ComplaintRequest complaintRequest) {
        String url = baseUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", complaintRequest.getTitle());
        body.add("description", complaintRequest.getDescription());
        // body.add("date", complaintRequest.getDate());
        body.add("date", new SimpleDateFormat("yyyy-MM-dd").format(complaintRequest.getDate()));
        body.add("categoryId", complaintRequest.getCategoryId().toString());

        for (MultipartFile file : complaintRequest.getFiles()) {
            body.add("files", file.getResource());
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Complaint>() {
        }).getBody();
    }

    public List<Attachment> getAttachmentsByComplaintId(Integer complaintId) {
        String url = baseUrl + "/" + complaintId + "/attachments";
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Attachment>>() {
                }).getBody();
    }

    public Complaint update(Integer id, ComplaintRequest complaintRequest) {
        String url = baseUrl + "/" + id;
        return restTemplate.exchange(url, HttpMethod.PUT,
                new HttpEntity<>(complaintRequest),
                new ParameterizedTypeReference<Complaint>() {
                }).getBody();
    }

    public Complaint delete(Integer id) {
        String url = baseUrl + "/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Complaint>() {
                }).getBody();
    }
}