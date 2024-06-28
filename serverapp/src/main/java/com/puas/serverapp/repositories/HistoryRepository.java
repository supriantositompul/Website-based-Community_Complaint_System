package com.puas.serverapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.puas.serverapp.models.entities.History;
import com.puas.serverapp.models.entities.User;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findByComplaintId(Integer complaintId);

    List<History> findByComplaintUser(User user);
}
