package com.puas.clientapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.puas.clientapp.models.History;
import com.puas.clientapp.services.HistoryService;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public String history(Model model) {
        List<History> histories = historyService.getAllHistoryByUser();
        model.addAttribute("histories", histories);
        model.addAttribute("isActive", "riwayat");
        return "history/history";
    }

    @GetMapping("/{id}")
    public String getByIdHistory(@PathVariable Integer id, Model model) {
        History historyResponse = historyService.getByIdWithDTO(id);
        model.addAttribute("history", historyResponse);
        model.addAttribute("isActive", "riwayat");
        return "history/history-detail";
    }
}
