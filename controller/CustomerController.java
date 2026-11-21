
package com.acxiom.crm.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acxiom.crm.entity.Customer;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.service.CustomerService;
import com.acxiom.crm.service.UserService;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    public CustomerController(
            CustomerService customerService,
            UserService userService) {

        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping
    public String listCustomers(
            @RequestParam(required = false) String search,
            Model model) {

        if (search != null && !search.isBlank()) {
            model.addAttribute("customers",
                    customerService.searchCustomers(search));
        } else {
            model.addAttribute("customers",
                    customerService.getAllCustomers());
        }

        model.addAttribute("search", search);
        return "customers";
    }

    @GetMapping("/new")
    public String newCustomer(Model model) {

        model.addAttribute("customer", new Customer());
        return "customers";
    }

    @PostMapping("/save")
    public String saveCustomer(
            @ModelAttribute Customer customer,
            Principal principal) {

        User owner = userService.getUserByEmail(principal.getName());

        customer.setOwner(owner);
        customerService.saveCustomer(customer);

        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("customer",
                customerService.getCustomerById(id));

        return "customers";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(
            @PathVariable Long id,
            @ModelAttribute Customer customer) {

        customerService.updateCustomer(id, customer);

        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);

        return "redirect:/customers";
    }
}
