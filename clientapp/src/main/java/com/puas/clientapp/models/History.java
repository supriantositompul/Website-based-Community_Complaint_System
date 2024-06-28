package com.puas.clientapp.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {

  private Integer id;
  private Date date;
  private String note;

  private Complaint complaint;
  private Status status;
}

