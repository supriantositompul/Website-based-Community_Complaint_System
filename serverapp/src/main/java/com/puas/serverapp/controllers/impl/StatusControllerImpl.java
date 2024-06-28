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
import com.puas.serverapp.models.dto.response.StatusResponse;
import com.puas.serverapp.models.entities.Status;
import com.puas.serverapp.services.impl.StatusServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/status")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class StatusControllerImpl implements GenericController<Status, Integer> {

    private StatusServiceImpl statusServiceImpl;

    @Override
    public List<Status> getAll() {
        return statusServiceImpl.getAll();
    }

    @GetMapping
    public List<StatusResponse> getAllWitDTO() {
        return statusServiceImpl.getAllStatusResponse();
    }

    @GetMapping("/{id}")
    @Override
    public Status getById(Integer id) {
        return statusServiceImpl.getById(id);
    }

    @PostMapping
    @Override
    @PreAuthorize("hasAuthority('CREATE_ADMIN')")
    public Status create(@RequestBody Status status) {
        return statusServiceImpl.create(status);
    }

    @PutMapping("/{id}")
    @Override
    public Status update(@PathVariable Integer id, @RequestBody Status status) {
        return statusServiceImpl.update(id, status);
    }

    @DeleteMapping("/{id}")
    @Override
    public Status delete(@PathVariable Integer id) {
        return statusServiceImpl.delete(id);
    }

}
