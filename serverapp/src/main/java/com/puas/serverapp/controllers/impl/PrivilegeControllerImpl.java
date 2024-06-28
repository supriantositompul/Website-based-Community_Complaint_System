package com.puas.serverapp.controllers.impl;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puas.serverapp.controllers.GenericController;
import com.puas.serverapp.models.entities.Privilege;
import com.puas.serverapp.services.impl.PrivilegeServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/privilege")
public class PrivilegeControllerImpl implements GenericController<Privilege, Integer>{
    
    private PrivilegeServiceImpl privilegeServiceImpl;

    @Override
    @GetMapping
    public List<Privilege> getAll() {
        return privilegeServiceImpl.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Privilege getById(@PathVariable Integer id) {
        return privilegeServiceImpl.getById(id);
    }

    @Override
    @PostMapping
    public Privilege create(@RequestBody Privilege privilege) {
        return privilegeServiceImpl.create(privilege);
    }

    @Override
    public Privilege update(Integer id, Privilege privilege) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public Privilege delete(@PathVariable Integer id) {
        return privilegeServiceImpl.delete(id);
    }
    
}
