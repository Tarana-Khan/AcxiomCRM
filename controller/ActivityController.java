
package com.acxiom.crm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acxiom.crm.entity.Activity;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.ActivityType;
import com.acxiom.crm.service.ActivityService;
import com.acxiom.crm.service.CustomerService;
import com.acxiom.crm.service.LeadService;
import com.acxiom.crm.service.UserService;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final CustomerService customerService;
    private final LeadService leadService;
    private final UserService userService;

    public ActivityController(
            ActivityService activityService,
            CustomerService customerService,
            LeadService leadService,
            UserService userService) {

        this.activityService = activityService;
        this.customerService = customerService;
        this.leadService = leadService;
        this.userService = userService;
    }

    @GetMapping
    public String listActivities(Model model) {

        model.addAttribute("activities",
                activityService.getAllActivities());

        model.addAttribute("types", ActivityType.values());

        return "activities";
    }

    @GetMapping("/new")
    public String newActivity(Model model) {

        model.addAttribute("activity", new Activity());
        addFormOptions(model);

        return "activities";
    }

    @PostMapping("/save")
    public String saveActivity(
            @ModelAttribute Activity activity,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long leadId,
            Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        activity.setUser(user);

        if (customerId != null) {
            activity.setCustomer(
                    customerService.getCustomerById(customerId));
        }

        if (leadId != null) {
            activity.setLead(leadService.getLeadById(leadId));
        }

        activityService.saveActivity(activity);

        return "redirect:/activities";
    }

    @GetMapping("/edit/{id}")
    public String editActivity(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("activity",
                activityService.getActivityById(id));

        addFormOptions(model);

        return "activities";
    }

    @PostMapping("/update/{id}")
    public String updateActivity(
            @PathVariable Long id,
            @ModelAttribute Activity activity,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long leadId) {

        if (customerId != null) {
            activity.setCustomer(
                    customerService.getCustomerById(customerId));
        }

        if (leadId != null) {
            activity.setLead(leadService.getLeadById(leadId));
        }

        activityService.updateActivity(id, activity);

        return "redirect:/activities";
    }

    @PostMapping("/delete/{id}")
    public String deleteActivity(@PathVariable Long id) {

        activityService.deleteActivity(id);
        return "redirect:/activities";
    }

    private void addFormOptions(Model model) {

        model.addAttribute("customers",
                customerService.getAllCustomers());

        model.addAttribute("leads",
                leadService.getAllLeads());

        model.addAttribute("types", ActivityType.values());
    }
}
