package com.puas.serverapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.puas.serverapp.models.entities.Privilege;
import com.puas.serverapp.repositories.PrivilegeRepository;
import com.puas.serverapp.services.GenericService;

@Service
public class PrivilegeServiceImpl implements GenericService<Privilege, Integer> {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public List<Privilege> getAll() {
        return privilegeRepository.findAll();
    }

    @Override
    public Privilege getById(@PathVariable Integer id) {
        return privilegeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "privilege not found"));
    }

    @Override
    public Privilege create(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

    @Override
    public Privilege update(Integer id, Privilege privilege) {
        return null;
    }

    @Override
    public Privilege delete(Integer id) {
        Privilege role = getById(id);
        privilegeRepository.delete(role);
        return role;
    }

}
