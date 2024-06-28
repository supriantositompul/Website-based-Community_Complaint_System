package com.puas.clientapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// @RequestMapping("/dashboard")
public class DashboardController {

  @GetMapping("/dashboard")
  public String dashboard(Model model) {
    return "dashboard/dashboard";
  }

  @GetMapping("/categoryDashboard")
  public String category(Model model) {
    return "dashboard/category";
  }

  @GetMapping("/pengaduan")
  public String pengaduan(Model model) {
    return "dashboard/pengaduan";
  }
  @GetMapping("/riwayat")
  public String riwayat(Model model) {
    return "dashboard/riwayat";
  }
  @GetMapping("/role")
  public String role(Model model) {
    return "dashboard/role";
  }
  @GetMapping("/user")
  public String user(Model model) {
    return "dashboard/user";
  }

}
