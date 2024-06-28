package com.puas.clientapp.models.dto.request;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.puas.clientapp.models.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintRequest {
    private String title;
    private String description;
    private Date date;
    private Integer categoryId;
    private Status status;
    private List<MultipartFile> files;
}
