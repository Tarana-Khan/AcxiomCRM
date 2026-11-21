
package com.acxiom.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.acxiom.crm.enums.LeadStatus;
import com.acxiom.crm.enums.OpportunityStage;
import com.acxiom.crm.service.CustomerService;
import com.acxiom.crm.service.FollowUpService;
import com.acxiom.crm.service.LeadService;
import com.acxiom.crm.service.OpportunityService;

@Controller
public class DashboardController {

    private final CustomerService customerService;
    private final LeadService leadService;
    private final OpportunityService opportunityService;
    private final FollowUpService followUpService;

    public DashboardController(
            CustomerService customerService,
            LeadService leadService,
            OpportunityService opportunityService,
            FollowUpService followUpService) {

        this.customerService = customerService;
        this.leadService = leadService;
        this.opportunityService = opportunityService;
        this.followUpService = followUpService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {

        model.addAttribute("totalCustomers",
                customerService.getTotalCustomers());

        model.addAttribute("activeCustomers",
                customerService.getActiveCustomerCount());

        model.addAttribute("totalLeads",
                leadService.getTotalLeads());

        model.addAttribute("newLeads",
                leadService.getLeadCountByStatus(LeadStatus.NEW));

        model.addAttribute("totalOpportunities",
                opportunityService.getTotalOpportunities());

        model.addAttribute("wonOpportunities",
                opportunityService.getOpportunityCountByStage(
                        OpportunityStage.WON));

        model.addAttribute("pipelineValue",
                opportunityService.getTotalOpportunityValue());

        model.addAttribute("pendingFollowUps",
                followUpService.getPendingFollowUpCount());

        return "dashboard";
    }
}
