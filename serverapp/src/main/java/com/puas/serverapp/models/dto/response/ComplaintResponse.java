package com.puas.serverapp.models.dto.response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintResponse {
    private Integer id;
    private String title;
    private Date date;
    private String description;
    private CategoryResponse category;
    private StatusResponse status;
    private List<AttachmentResponse> attachments;
    private UserResponse user;
}
