package com.puas.serverapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.puas.serverapp.models.entities.Role;
import com.puas.serverapp.repositories.RoleRepository;
import com.puas.serverapp.services.GenericService;

@Service
public class RoleServiceImpl implements GenericService<Role, Integer>{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getById(Integer id) {
                return roleRepository.findById(id).orElseThrow(() ->
         new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")
        );
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Integer id, Role role) {
        getById(id);
        role.setId(id);
        return roleRepository.save(role);

    }

    @Override
    public Role delete(Integer id) {
        Role role = getById(id);
        roleRepository.delete(role);
        return role;
    }

}
