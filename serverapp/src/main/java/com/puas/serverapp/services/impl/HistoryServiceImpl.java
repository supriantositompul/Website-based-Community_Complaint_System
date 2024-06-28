package com.puas.serverapp.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.puas.serverapp.models.dto.response.ComplaintResponse;
import com.puas.serverapp.models.dto.response.HistoryResponse;
import com.puas.serverapp.models.dto.response.StatusResponse;
import com.puas.serverapp.models.entities.History;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.repositories.HistoryRepository;
import com.puas.serverapp.services.GenericService;

@Service
public class HistoryServiceImpl implements GenericService<History, Integer> {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<History> getAll() {
        return historyRepository.findAll();
    }

    public List<HistoryResponse> getAllHistoryResponses() {
        List<History> histories = getAll();
        return histories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HistoryResponse> getHistoriesByUser(User user) {
        List<History> histories = historyRepository.findByComplaintUser(user);
        return histories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public List<HistoryResponse> getHistoryResponsesByComplaintId(Integer complaintId) {
        List<History> histories = getByComplaintId(complaintId);
        return histories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public History getById(Integer id) {
        return historyRepository.findById(id).orElseThrow(() -> new RuntimeException("History not found!"));
    }

    public HistoryResponse getByIdWithDTO(Integer id) {
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found!"));
        return convertToDTO(history);
    }


    public List<History> getByComplaintId(Integer complaintId) {
        return historyRepository.findByComplaintId(complaintId);
    }

    @Override
    public History create(History history) {
        return null;
    }

    @Override
    public History update(Integer id, History history) {
        History existingHistory = getById(id);

        if (existingHistory != null) {
            existingHistory.setDate(history.getDate());
            existingHistory.setNote(history.getNote());
            existingHistory.setComplaint(history.getComplaint());
            existingHistory.setStatus(history.getStatus());
            return historyRepository.save(existingHistory);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found");
        }
    }

    @Override
    public History delete(Integer id) {
        History history = getById(id);
        historyRepository.delete(history);
        return history;
    }

    public HistoryResponse convertToDTO(History history) {
        HistoryResponse historyResponse = modelMapper.map(history, HistoryResponse.class);
        historyResponse.setComplaint(modelMapper.map(history.getComplaint(), ComplaintResponse.class));
        historyResponse.setStatus(modelMapper.map(history.getStatus(), StatusResponse.class));
        return historyResponse;
    }

}
