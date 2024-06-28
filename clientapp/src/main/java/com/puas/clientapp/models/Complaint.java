package com.puas.clientapp.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

  private Integer id;
  private String title;
  private Date date;
  private String description; 
  private Category category;
  
  @JsonProperty("status")
  private Status status;
  private List<Attachment> attachments;
}

