package com.puas.serverapp.controllers.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puas.serverapp.controllers.GenericController;
import com.puas.serverapp.models.dto.request.HistoryRequest;
import com.puas.serverapp.models.dto.response.ApiResponse;
import com.puas.serverapp.models.dto.response.HistoryResponse;
import com.puas.serverapp.models.entities.Complaint;
import com.puas.serverapp.models.entities.History;
import com.puas.serverapp.models.entities.Status;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.repositories.ComplaintRepository;
import com.puas.serverapp.services.impl.ComplaintServiceImpl;
import com.puas.serverapp.services.impl.HistoryServiceImpl;
import com.puas.serverapp.services.impl.StatusServiceImpl;
import com.puas.serverapp.services.impl.UserServiceImpl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@RestController
@AllArgsConstructor
@RequestMapping("/history")
public class HistoryControllerImpl implements GenericController<History, Integer> {

    @Autowired
    private HistoryServiceImpl historyServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private ComplaintServiceImpl complaintServiceImpl;
    @Autowired
    private StatusServiceImpl statusServiceImpl;
    @Autowired
    private ComplaintRepository complaintRepository;

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    // @GetMapping
    public List<History> getAll() {
        return historyServiceImpl.getAll();
    }

    // @GetMapping
    public ResponseEntity<List<HistoryResponse>> getAllDTO() {
        List<HistoryResponse> historyResponses = historyServiceImpl.getAllHistoryResponses();
        return ResponseEntity.ok(historyResponses);
    }

    @Override
    public History getById(@PathVariable Integer id) {
        return historyServiceImpl.getById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HistoryResponse>> getByIdwithDTO(@PathVariable Integer id, Principal principal) {
        User currentUser = userServiceImpl.getByUsername(principal.getName());
        HistoryResponse historyResponse = historyServiceImpl.getByIdWithDTO(id);

        // Cek apakah pengguna adalah admin atau pemilik dari komplain
        if (!currentUser.getRoles().contains("ROLE_ADMIN")
                && !currentUser.getId().equals(historyResponse.getComplaint().getUser().getId())) {
            ApiResponse<HistoryResponse> response = new ApiResponse<>(
                    HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to view this history",
                    getCurrentTimestamp(),
                    null,
                    null,
                    null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        ApiResponse<HistoryResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "History fetched successfully",
                getCurrentTimestamp(),
                historyResponse,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getUserHistories(Principal principal) {
        User currentUser = userServiceImpl.getByUsername(principal.getName());
        List<HistoryResponse> historyResponses = historyServiceImpl.getHistoriesByUser(currentUser);
        ApiResponse<List<HistoryResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "History fetched successfully",
                getCurrentTimestamp(),
                historyResponses,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getAllHistoriesForAdmin() {
        List<HistoryResponse> historyResponses = historyServiceImpl.getAllHistoryResponses();
        ApiResponse<List<HistoryResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "History fetched successfully",
                getCurrentTimestamp(),
                historyResponses,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byComplaint/{complaintId}")
    public ResponseEntity<List<HistoryResponse>> getByComplaintIdwithDTO(@PathVariable Integer complaintId) {
        List<HistoryResponse> historyResponses = historyServiceImpl.getHistoryResponsesByComplaintId(complaintId);
        return ResponseEntity.ok(historyResponses);
    }

    @Override
    public History create(History history) {
        return null;
    }

    @Override
    public History update(@PathVariable Integer id, @RequestBody History history) {
        return historyServiceImpl.update(id, history);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<History>> updateHistory(@PathVariable Integer id,
            @RequestBody HistoryRequest history,
            Principal principal) {
        History existingHistory = historyServiceImpl.getById(id);

        if (existingHistory == null) {
            ApiResponse<History> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "History not found",
                    getCurrentTimestamp(),
                    null,
                    null,
                    null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // fetch complaint and status
        Complaint existingComplaint = complaintServiceImpl.getById(history.getComplaintId());
        Status newStatus = statusServiceImpl.getById(history.getStatusId());

        // Check if complaint and status exist
        if (existingComplaint == null || newStatus == null) {
            ApiResponse<History> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Complaint or Status not found", getCurrentTimestamp(), null, null, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // fetch current user
        User currentUser = userServiceImpl.getByUsername(principal.getName());

        // authorization check
        if (!"Waiting".equals(existingHistory.getStatus().getName())
                && !currentUser.getRoles().contains("ROLE_ADMIN")) {
            ApiResponse<History> response = new ApiResponse<>(
                    HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to update this history",
                    getCurrentTimestamp(),
                    null,
                    null,
                    null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        existingHistory.setDate(history.getDate());
        existingHistory.setNote(history.getNote());
        existingHistory.setComplaint(existingComplaint);
        existingHistory.setStatus(newStatus);

        // Update the complaint's status
        existingComplaint.setStatus(newStatus);
        complaintRepository.save(existingComplaint);

        // Save the updated history
        History updatedHistory;
        try {
            updatedHistory = historyServiceImpl.update(id, existingHistory);
        } catch (Exception e) {
            // Handle any unexpected exceptions that may occur during update
            ApiResponse<History> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update history: " + e.getMessage(), getCurrentTimestamp(), null, null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        ApiResponse<History> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "History updated successfully",
                getCurrentTimestamp(),
                updatedHistory,
                null,
                null);
        return ResponseEntity.ok(response);
    }

    @Override
    public History delete(@PathVariable Integer id) {
        return historyServiceImpl.delete(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<History>> delete(@PathVariable Integer id, Principal principal) {
        History existingHistory = historyServiceImpl.getById(id);
        User currentUser = userServiceImpl.getByUsername(principal.getName());

        if (existingHistory == null) {
            ApiResponse<History> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "History not found",
                    getCurrentTimestamp(),
                    null,
                    null,
                    null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!currentUser.getRoles().contains("ROLE_ADMIN")
                && !currentUser.getId().equals(existingHistory.getComplaint().getUser().getId())) {
            ApiResponse<History> response = new ApiResponse<>(
                    HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to delete this history",
                    getCurrentTimestamp(),
                    null,
                    null,
                    null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        History deletedHistory = historyServiceImpl.delete(id);
        ApiResponse<History> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "History deleted successfully",
                getCurrentTimestamp(),
                deletedHistory,
                null,
                null);
        return ResponseEntity.ok(response);
    }
}
