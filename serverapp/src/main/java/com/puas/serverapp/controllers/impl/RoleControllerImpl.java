package com.puas.serverapp.controllers.impl;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puas.serverapp.controllers.GenericController;
import com.puas.serverapp.models.entities.Role;
import com.puas.serverapp.services.impl.RoleServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/role")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class RoleControllerImpl implements GenericController<Role, Integer> {

    private RoleServiceImpl roleServiceImpl;

    @Override
    @GetMapping
    public List<Role> getAll() {
        return roleServiceImpl.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Role getById(@PathVariable Integer id) {
        return roleServiceImpl.getById(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ADMIN')")
    public Role create(@RequestBody Role role) {
        return roleServiceImpl.create(role);
    }

    @Override
    @PutMapping("/{id}")
    public Role update(@PathVariable Integer id, @RequestBody Role role) {
        return roleServiceImpl.update(id, role);
    }

    @Override
    @DeleteMapping("/{id}")
    public Role delete(@PathVariable Integer id) {
        return roleServiceImpl.delete(id);
    }

}
