package com.puas.serverapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.puas.serverapp.models.entities.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{
    
}
