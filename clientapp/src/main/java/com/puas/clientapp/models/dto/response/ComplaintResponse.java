package com.puas.clientapp.models.dto.response;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ComplaintResponse {
    private Integer id;
    private String title;
    private String description;
    private Date date;
    private String categoryId; 
    private List<MultipartFile> files;
}
