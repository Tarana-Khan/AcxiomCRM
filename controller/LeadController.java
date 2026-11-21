
package com.acxiom.crm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acxiom.crm.entity.Lead;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.LeadStatus;
import com.acxiom.crm.service.LeadService;
import com.acxiom.crm.service.UserService;

@Controller
@RequestMapping("/leads")
public class LeadController {

    private final LeadService leadService;
    private final UserService userService;

    public LeadController(LeadService leadService, UserService userService) {
        this.leadService = leadService;
        this.userService = userService;
    }

    @GetMapping
    public String listLeads(
            @RequestParam(required = false) String search,
            Model model) {

        model.addAttribute("leads",
                search != null && !search.isBlank()
                        ? leadService.searchLeads(search)
                        : leadService.getAllLeads());

        model.addAttribute("statuses", LeadStatus.values());
        model.addAttribute("search", search);

        return "leads";
    }

    @GetMapping("/new")
    public String newLead(Model model) {
        model.addAttribute("lead", new Lead());
        model.addAttribute("statuses", LeadStatus.values());
        return "leads";
    }

    @PostMapping("/save")
    public String saveLead(
            @ModelAttribute Lead lead,
            Principal principal) {

        User owner = userService.getUserByEmail(principal.getName());
        lead.setOwner(owner);

        leadService.saveLead(lead);

        return "redirect:/leads";
    }

    @GetMapping("/edit/{id}")
    public String editLead(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("lead", leadService.getLeadById(id));
        model.addAttribute("statuses", LeadStatus.values());

        return "leads";
    }

    @PostMapping("/update/{id}")
    public String updateLead(
            @PathVariable Long id,
            @ModelAttribute Lead lead) {

        leadService.updateLead(id, lead);
        return "redirect:/leads";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam LeadStatus status) {

        leadService.updateLeadStatus(id, status);
        return "redirect:/leads";
    }

    @PostMapping("/delete/{id}")
    public String deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return "redirect:/leads";
    }
}
