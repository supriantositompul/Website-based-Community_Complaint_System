package com.puas.serverapp.models.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRequest {
    private Integer id;
    private Date date;
    private String note;
    private Integer ComplaintId;
    private Integer statusId;
}
