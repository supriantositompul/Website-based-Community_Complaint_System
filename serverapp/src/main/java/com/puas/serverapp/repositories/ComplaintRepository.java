package com.puas.serverapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.puas.serverapp.models.entities.Complaint;
import com.puas.serverapp.models.entities.User;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    Optional<Complaint> findByTitle(String title);

    Boolean existsByTitleAndId(String title, Integer id);

    List<Complaint> findByUserId(Integer userId);

    List<Complaint> findByUser(User user);
}
