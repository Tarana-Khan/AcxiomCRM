
package com.acxiom.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acxiom.crm.enums.CustomerStatus;
import com.acxiom.crm.enums.FollowUpStatus;
import com.acxiom.crm.enums.LeadStatus;
import com.acxiom.crm.enums.OpportunityStage;
import com.acxiom.crm.service.AuditService;
import com.acxiom.crm.service.CustomerService;
import com.acxiom.crm.service.FollowUpService;
import com.acxiom.crm.service.LeadService;
import com.acxiom.crm.service.OpportunityService;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final CustomerService customerService;
    private final LeadService leadService;
    private final OpportunityService opportunityService;
    private final FollowUpService followUpService;
    private final AuditService auditService;

    public ReportController(
            CustomerService customerService,
            LeadService leadService,
            OpportunityService opportunityService,
            FollowUpService followUpService,
            AuditService auditService) {

        this.customerService = customerService;
        this.leadService = leadService;
        this.opportunityService = opportunityService;
        this.followUpService = followUpService;
        this.auditService = auditService;
    }

    @GetMapping
    public String reports(Model model) {

        model.addAttribute("totalCustomers",
                customerService.getTotalCustomers());

        model.addAttribute("activeCustomers",
                customerService.getActiveCustomerCount());

        model.addAttribute("totalLeads",
                leadService.getTotalLeads());

        model.addAttribute("newLeads",
                leadService.getLeadCountByStatus(LeadStatus.NEW));

        model.addAttribute("contactedLeads",
                leadService.getLeadCountByStatus(LeadStatus.CONTACTED));

        model.addAttribute("qualifiedLeads",
                leadService.getLeadCountByStatus(LeadStatus.QUALIFIED));

        model.addAttribute("convertedLeads",
                leadService.getLeadCountByStatus(LeadStatus.CONVERTED));

        model.addAttribute("lostLeads",
                leadService.getLeadCountByStatus(LeadStatus.LOST));

        model.addAttribute("totalOpportunities",
                opportunityService.getTotalOpportunities());

        model.addAttribute("wonOpportunities",
                opportunityService.getOpportunityCountByStage(
                        OpportunityStage.WON));

        model.addAttribute("lostOpportunities",
                opportunityService.getOpportunityCountByStage(
                        OpportunityStage.LOST));

        model.addAttribute("pipelineValue",
                opportunityService.getTotalOpportunityValue());

        model.addAttribute("pendingFollowUps",
                followUpService.getFollowUpsByStatus(
                        FollowUpStatus.PENDING).size());

        model.addAttribute("completedFollowUps",
                followUpService.getFollowUpsByStatus(
                        FollowUpStatus.COMPLETED).size());

        model.addAttribute("auditLogs",
                auditService.getAllAuditLogs());

        model.addAttribute("customerStatuses",
                CustomerStatus.values());

        return "reports";
    }
}
