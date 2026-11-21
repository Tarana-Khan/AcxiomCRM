
package com.acxiom.crm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acxiom.crm.entity.FollowUp;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.FollowUpStatus;
import com.acxiom.crm.service.CustomerService;
import com.acxiom.crm.service.FollowUpService;
import com.acxiom.crm.service.LeadService;
import com.acxiom.crm.service.UserService;

@Controller
@RequestMapping("/followups")
public class FollowUpController {

    private final FollowUpService followUpService;
    private final CustomerService customerService;
    private final LeadService leadService;
    private final UserService userService;

    public FollowUpController(
            FollowUpService followUpService,
            CustomerService customerService,
            LeadService leadService,
            UserService userService) {

        this.followUpService = followUpService;
        this.customerService = customerService;
        this.leadService = leadService;
        this.userService = userService;
    }

    @GetMapping
    public String listFollowUps(Model model) {

        model.addAttribute("followups",
                followUpService.getAllFollowUps());

        model.addAttribute("statuses", FollowUpStatus.values());

        return "followups";
    }

    @GetMapping("/new")
    public String newFollowUp(Model model) {

        model.addAttribute("followup", new FollowUp());
        addFormOptions(model);

        return "followups";
    }

    @PostMapping("/save")
    public String saveFollowUp(
            @ModelAttribute FollowUp followup,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long leadId,
            Principal principal) {

        User assignedTo =
                userService.getUserByEmail(principal.getName());

        followup.setAssignedTo(assignedTo);

        if (customerId != null) {
            followup.setCustomer(
                    customerService.getCustomerById(customerId));
        }

        if (leadId != null) {
            followup.setLead(leadService.getLeadById(leadId));
        }

        followUpService.saveFollowUp(followup);

        return "redirect:/followups";
    }

    @GetMapping("/edit/{id}")
    public String editFollowUp(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("followup",
                followUpService.getFollowUpById(id));

        addFormOptions(model);

        return "followups";
    }

    @PostMapping("/update/{id}")
    public String updateFollowUp(
            @PathVariable Long id,
            @ModelAttribute FollowUp followup,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long leadId) {

        if (customerId != null) {
            followup.setCustomer(
                    customerService.getCustomerById(customerId));
        }

        if (leadId != null) {
            followup.setLead(leadService.getLeadById(leadId));
        }

        followUpService.updateFollowUp(id, followup);

        return "redirect:/followups";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam FollowUpStatus status) {

        followUpService.updateFollowUpStatus(id, status);
        return "redirect:/followups";
    }

    @PostMapping("/delete/{id}")
    public String deleteFollowUp(@PathVariable Long id) {

        followUpService.deleteFollowUp(id);
        return "redirect:/followups";
    }

    private void addFormOptions(Model model) {

        model.addAttribute("customers",
                customerService.getAllCustomers());

        model.addAttribute("leads",
                leadService.getAllLeads());

        model.addAttribute("statuses",
                FollowUpStatus.values());
    }
}
