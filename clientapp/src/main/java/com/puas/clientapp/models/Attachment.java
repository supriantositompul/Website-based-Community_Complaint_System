package com.puas.clientapp.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

  private Integer id;
  private String fileName;
  private String fileType;
  private byte[] data;
  
}

