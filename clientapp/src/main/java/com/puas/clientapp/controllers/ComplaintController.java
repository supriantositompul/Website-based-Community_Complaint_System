package com.puas.clientapp.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.puas.clientapp.models.dto.request.ComplaintRequest;
import com.puas.clientapp.services.CategoryService;
import com.puas.clientapp.services.ComplaintService;

@Controller // * html
@RequestMapping("/complaint")
public class ComplaintController {

  @Autowired
  private ComplaintService complaintService;

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public String createView(Model model) {
    model.addAttribute("isActive", "pengaduan");
    model.addAttribute("categories", categoryService.getAll());
    return "complaint/complaint-form";
  }

  @PostMapping
  public String createComplaint(
      @RequestParam("title") String title,
      @RequestParam("description") String description,
      @RequestParam("date") String dateString,
      // @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date date,
      @RequestParam("categoryId") Integer categoryId,
      @RequestParam("files") List<MultipartFile> files,
      Model model) {

    try {

      Date date;
      try {
        // Attempt to parse date as yyyy-MM-dd
        date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
      } catch (ParseException e) {
        // If parsing fails, try to parse it as a timestamp
        date = new Date(Long.parseLong(dateString));
      }

      ComplaintRequest request = new ComplaintRequest();
      request.setTitle(title);
      request.setDescription(description);
      request.setDate(date);
      request.setCategoryId(categoryId);
      request.setFiles(files);

      complaintService.create(request);

      model.addAttribute("isActive", "pengaduan");
      return "redirect:/history/history";
    } catch (Exception e) {
      model.addAttribute("isActive", "pengaduan");
      e.printStackTrace();
      model.addAttribute("error", "Error creating complaint: " + e.getMessage());
      return "complaint/complaint-form";
    }
  }

  // @GetMapping("/complaint-detail")
  // public String detail(Model model) {
  // model.addAttribute("isActive", "pengaduan");
  // return "complaint/complaint-detail";
  // }

  // @GetMapping("/complaint-response")
  // public String response(Model model) {
  // model.addAttribute("isActive", "pengaduan");
  // return "complaint/complaint-response";
  // }

}
