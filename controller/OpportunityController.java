
package com.acxiom.crm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acxiom.crm.entity.Opportunity;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.OpportunityStage;
import com.acxiom.crm.service.CustomerService;
import com.acxiom.crm.service.OpportunityService;
import com.acxiom.crm.service.UserService;

@Controller
@RequestMapping("/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;
    private final CustomerService customerService;
    private final UserService userService;

    public OpportunityController(
            OpportunityService opportunityService,
            CustomerService customerService,
            UserService userService) {

        this.opportunityService = opportunityService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping
    public String listOpportunities(Model model) {

        model.addAttribute("opportunities",
                opportunityService.getAllOpportunities());

        model.addAttribute("stages", OpportunityStage.values());

        return "opportunities";
    }

    @GetMapping("/new")
    public String newOpportunity(Model model) {

        model.addAttribute("opportunity", new Opportunity());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("stages", OpportunityStage.values());

        return "opportunities";
    }

    @PostMapping("/save")
    public String saveOpportunity(
            @ModelAttribute Opportunity opportunity,
            @RequestParam(required = false) Long customerId,
            Principal principal) {

        User owner = userService.getUserByEmail(principal.getName());
        opportunity.setOwner(owner);

        if (customerId != null) {
            opportunity.setCustomer(
                    customerService.getCustomerById(customerId));
        }

        opportunityService.saveOpportunity(opportunity);

        return "redirect:/opportunities";
    }

    @GetMapping("/edit/{id}")
    public String editOpportunity(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("opportunity",
                opportunityService.getOpportunityById(id));

        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("stages", OpportunityStage.values());

        return "opportunities";
    }

    @PostMapping("/update/{id}")
    public String updateOpportunity(
            @PathVariable Long id,
            @ModelAttribute Opportunity opportunity,
            @RequestParam(required = false) Long customerId) {

        if (customerId != null) {
            opportunity.setCustomer(
                    customerService.getCustomerById(customerId));
        }

        opportunityService.updateOpportunity(id, opportunity);

        return "redirect:/opportunities";
    }

    @PostMapping("/stage/{id}")
    public String updateStage(
            @PathVariable Long id,
            @RequestParam OpportunityStage stage) {

        opportunityService.updateStage(id, stage);
        return "redirect:/opportunities";
    }

    @PostMapping("/delete/{id}")
    public String deleteOpportunity(@PathVariable Long id) {

        opportunityService.deleteOpportunity(id);
        return "redirect:/opportunities";
    }
}
