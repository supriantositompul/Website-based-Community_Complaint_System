package com.puas.serverapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.puas.serverapp.models.entities.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer>{
    List<Attachment> findByComplaintId(Integer complaintId);
}
