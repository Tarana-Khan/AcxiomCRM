
package com.acxiom.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.acxiom.crm.dto.UserRequest;
import com.acxiom.crm.enums.RoleName;
import com.acxiom.crm.repository.RoleRepository;
import com.acxiom.crm.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(
            UserService userService,
            RoleRepository roleRepository) {

        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listUsers(Model model) {

        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {

        model.addAttribute("userRequest", new UserRequest());
        model.addAttribute("roles", roleRepository.findAll());

        return "users";
    }

    @PostMapping("/save")
    public String saveUser(
            @ModelAttribute UserRequest userRequest) {

        userService.createUser(userRequest);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(
            @PathVariable Long id,
            Model model) {

        var user = userService.getUserById(id);

        UserRequest request = new UserRequest();
        request.setId(user.getId());
        request.setFullName(user.getFullName());
        request.setEmail(user.getEmail());
        request.setRoleId(user.getRole().getId());

        model.addAttribute("userRequest", request);
        model.addAttribute("roles", roleRepository.findAll());

        return "users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute UserRequest userRequest) {

        userService.updateUser(id, userRequest);
        return "redirect:/users";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {

        userService.deactivateUser(id);
        return "redirect:/users";
    }

    @PostMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id) {

        userService.activateUser(id);
        return "redirect:/users";
    }
}
