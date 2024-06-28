package com.puas.serverapp.models.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private Integer id;
    private Date date;
    private String note;
    private ComplaintResponse complaint;
    private StatusResponse status;
}
