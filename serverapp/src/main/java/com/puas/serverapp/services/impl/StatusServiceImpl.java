package com.puas.serverapp.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.puas.serverapp.models.dto.response.StatusResponse;
import com.puas.serverapp.models.entities.Status;
import com.puas.serverapp.repositories.StatusRepository;
import com.puas.serverapp.services.GenericService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements GenericService<Status, Integer> {

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    // public List<HistoryResponse> getAllHistoryResponses() {
    // List<History> histories = getAll();
    // return histories.stream()
    // .map(this::convertToDTO)
    // .collect(Collectors.toList());
    // }

    public List<StatusResponse> getAllStatusResponse() {
        List<Status> status = getAll();
        return status.stream().map(this::converToDTO).collect(Collectors.toList());
    }

    @Override
    public Status getById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status not found"));
    }

    @Override
    public Status create(Status status) {
        if (statusRepository.findByName(status.getName()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "status name is already!");
        }
        return statusRepository.save(status);
    }

    @Override
    public Status update(Integer id, Status status) {
        Status existingStatus = getById(id);
        existingStatus.setId(id);

        if (statusRepository.existsByNameAndId(status.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "status is already exists!");
        }

        existingStatus.setName(status.getName());
        return statusRepository.save(existingStatus);
    }

    @Override
    public Status delete(Integer id) {
        Status status = getById(id);
        statusRepository.delete(status);
        return status;
    }

    public StatusResponse converToDTO(Status status) {
        StatusResponse statusResponse = modelMapper.map(status, StatusResponse.class);
        return statusResponse;
    }

}
