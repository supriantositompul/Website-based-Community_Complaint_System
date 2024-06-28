package com.puas.serverapp.controllers.impl;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.puas.serverapp.controllers.GenericController;
import com.puas.serverapp.models.dto.response.ApiResponse;
import com.puas.serverapp.models.dto.response.ComplaintResponse;
import com.puas.serverapp.models.entities.Attachment;
import com.puas.serverapp.models.entities.Category;
import com.puas.serverapp.models.entities.Complaint;
import com.puas.serverapp.models.entities.Status;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.services.impl.CategoryServiceImpl;
import com.puas.serverapp.services.impl.ComplaintServiceImpl;
import com.puas.serverapp.services.impl.StatusServiceImpl;
import com.puas.serverapp.services.impl.UserServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/complaint")
public class ComplaintControllerImpl implements GenericController<Complaint, Integer> {

    @Autowired
    private ComplaintServiceImpl complaintServiceImpl;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @Autowired
    private StatusServiceImpl statusServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public List<Complaint> getAll() {
        return complaintServiceImpl.getAll();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getUserComplaints(Principal principal) {
        User currentUser = userServiceImpl.getByUsername(principal.getName());
        List<ComplaintResponse> complaintResponses = complaintServiceImpl.getComplaintsByUser(currentUser);
        ApiResponse<List<ComplaintResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Complaint fetched successfully",
                getCurrentTimestamp(),
                complaintResponses,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getAllComplaintsForAdmin() {
        List<ComplaintResponse> complaintResponses = complaintServiceImpl.getAllDTO();
        ApiResponse<List<ComplaintResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Complaint fetched successfully",
                getCurrentTimestamp(),
                complaintResponses,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    // @GetMapping
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getAllDTO() {
        List<ComplaintResponse> complaintResponses = complaintServiceImpl.getAllDTO();
        ApiResponse<List<ComplaintResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Complaint fetched successfully",
                getCurrentTimestamp(),
                complaintResponses,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @Override
    public Complaint getById(@PathVariable Integer id) {
        return complaintServiceImpl.getById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ComplaintResponse>> getByIdWithDTO(@PathVariable Integer id) {
        try {
            ComplaintResponse complaintResponse = complaintServiceImpl.getByIdDTO(id);
            ApiResponse<ComplaintResponse> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Complaint by id fetched successfully",
                    getCurrentTimestamp(),
                    complaintResponse,
                    null,
                    null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ComplaintResponse> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Complaint not found",
                    getCurrentTimestamp(),
                    null,
                    null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse<Complaint>> createComplaint(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam("categoryId") String categoryIdStr,
            // @RequestParam("statusId") String statusIdStr,
            @RequestParam("files") List<MultipartFile> files) {

        Integer categoryId = Integer.parseInt(categoryIdStr);
        // Integer statusId = Integer.parseInt(statusIdStr);

        // Get Category and Status by ID
        Category category = categoryServiceImpl.getById(categoryId);

        // Set default status to waiting
        Status defaultStatus = statusServiceImpl.getById(1);

        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setDate(date);
        complaint.setCategory(category);
        complaint.setStatus(defaultStatus);

        try {
            Complaint savedComplaint = complaintServiceImpl.createWithAttachment(complaint, files);
            ApiResponse<Complaint> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Complaint created successfully",
                    getCurrentTimestamp(),
                    savedComplaint,
                    null,
                    null);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ApiResponse<Complaint> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error whille saving complaint",
                    getCurrentTimestamp(),
                    null,
                    null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{complaintId}/attachments")
    public ResponseEntity<List<Attachment>> getAttachmentsByComplaintId(@PathVariable Integer complaintId) {
        return ResponseEntity.ok(complaintServiceImpl.getAttachmentsByComplaintId(complaintId));
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponse<Complaint>> updateComplaintWithAttachment(
            @PathVariable Integer id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam("categoryId") String categoryIdStr,
            @RequestParam("statusId") String statusIdStr,
            @RequestParam("files") List<MultipartFile> files) {

        Integer categoryId = Integer.parseInt(categoryIdStr);
        Integer statusId = Integer.parseInt(statusIdStr);

        Category category = categoryServiceImpl.getById(categoryId);

        Status status = statusServiceImpl.getById(statusId);

        Complaint updatedComplaint = new Complaint();
        updatedComplaint.setTitle(title);
        updatedComplaint.setDescription(description);
        updatedComplaint.setDate(date);
        updatedComplaint.setCategory(category);
        updatedComplaint.setStatus(status);

        try {
            Complaint savedComplaint = complaintServiceImpl.updateWithAttachment(id, updatedComplaint, files);
            ApiResponse<Complaint> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Complaint updated successfully",
                    getCurrentTimestamp(),
                    savedComplaint,
                    null,
                    null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Complaint> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error while updating complaint",
                    getCurrentTimestamp(),
                    null,
                    null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public Complaint delete(@PathVariable Integer id) {
        return complaintServiceImpl.delete(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Complaint>> deleteComplaint(@PathVariable Integer id) {
        Complaint deletedComplaint = complaintServiceImpl.delete(id);
        ApiResponse<Complaint> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Complaint deleted successfully",
                getCurrentTimestamp(),
                deletedComplaint,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @Override
    public Complaint create(Complaint complaint) {
        return null;
    }

    @Override
    public Complaint update(Integer id, Complaint complaint) {
        return null;
    }
}
