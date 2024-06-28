package com.puas.serverapp.services.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.puas.serverapp.models.dto.response.AttachmentResponse;
import com.puas.serverapp.models.dto.response.CategoryResponse;
import com.puas.serverapp.models.dto.response.ComplaintResponse;
import com.puas.serverapp.models.dto.response.StatusResponse;
import com.puas.serverapp.models.entities.Attachment;
import com.puas.serverapp.models.entities.Complaint;
import com.puas.serverapp.models.entities.History;
import com.puas.serverapp.models.entities.Status;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.repositories.AttachmentRepository;
import com.puas.serverapp.repositories.ComplaintRepository;
import com.puas.serverapp.repositories.HistoryRepository;
import com.puas.serverapp.repositories.StatusRepository;
import com.puas.serverapp.repositories.UserRepository;
import com.puas.serverapp.services.GenericService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ComplaintServiceImpl implements GenericService<Complaint, Integer> {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Complaint> getAll() {
        return complaintRepository.findAll();
    }

    public List<ComplaintResponse> getAllDTO() {
        List<Complaint> complaints = getAll();
        return complaints.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getComplaintsByUser(User user) {
        List<Complaint> complaints = complaintRepository.findByUser(user);
        return complaints.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Complaint getById(Integer id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "complaint not found"));
    }

    public ComplaintResponse getByIdDTO(Integer id) {
        Complaint complaint = getById(id);
        return convertToDTO(complaint);
    }

    public List<Complaint> findByUserId(Integer userId) {
        return complaintRepository.findByUserId(userId);
    }

    @Override
    public Complaint create(Complaint complaint) {
        return null;
    }

    public Complaint createWithAttachment(Complaint complaint, List<MultipartFile> files) throws IOException {
        Status defaultStatus = statusRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found!"));
        complaint.setStatus(defaultStatus);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Set user 
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName);
        complaint.setUser(user);    


        Complaint savedComplaint = complaintRepository.save(complaint);

        // Add to history
        History history = new History();
        history.setComplaint(savedComplaint);
        history.setDate(new Date());
        history.setNote("Complaint created with status waiting");
        history.setStatus(defaultStatus);
        historyRepository.save(history);

        for (MultipartFile file : files) {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setData(file.getBytes());
            attachment.setComplaint(savedComplaint);
            attachmentRepository.save(attachment);
        }
        return savedComplaint;
    }

    public List<Attachment> getAttachmentsByComplaintId(Integer complaintId) {
        return attachmentRepository.findByComplaintId(complaintId);
    }

    public Complaint updateWithAttachment(Integer id, Complaint updatedComplaint, List<MultipartFile> files)
            throws IOException {
        Complaint existingComplaint = getById(id);

        existingComplaint.setTitle(updatedComplaint.getTitle());
        existingComplaint.setDescription(updatedComplaint.getDescription());
        existingComplaint.setDate(updatedComplaint.getDate());
        existingComplaint.setCategory(updatedComplaint.getCategory());
        existingComplaint.setStatus(updatedComplaint.getStatus());

        // Delete existing attachments if new files are provided
        if (!files.isEmpty()) {
            attachmentRepository.deleteAll(existingComplaint.getAttachments());
            for (MultipartFile file : files) {
                Attachment attachment = new Attachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileType(file.getContentType());
                attachment.setData(file.getBytes());
                attachment.setComplaint(existingComplaint);
                attachmentRepository.save(attachment);
            }
        }

        return complaintRepository.save(existingComplaint);
    }

    @Override
    public Complaint update(Integer id, Complaint complaint) {
        return null;
    }

    @Override
    public Complaint delete(Integer id) {
        Complaint complaint = getById(id);
        complaintRepository.delete(complaint);
        return complaint;
    }

    public ComplaintResponse convertToDTO(Complaint complaint) {
        ComplaintResponse complaintResponse = modelMapper.map(complaint, ComplaintResponse.class);
        complaintResponse.setCategory(modelMapper.map(complaint.getCategory(), CategoryResponse.class));
        complaintResponse.setStatus(modelMapper.map(complaint.getStatus(), StatusResponse.class));
        complaintResponse.setAttachments(
                complaint.getAttachments().stream()
                        .map(attachment -> modelMapper.map(attachment, AttachmentResponse.class))
                        .collect(Collectors.toList()));
        return complaintResponse;
    }

}
