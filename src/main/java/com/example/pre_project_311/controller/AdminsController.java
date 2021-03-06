package com.example.pre_project_311.controller;

import com.example.pre_project_311.model.Role;
import com.example.pre_project_311.model.User;
import com.example.pre_project_311.service.RoleService;
import com.example.pre_project_311.service.RoleServiceImpl;
import com.example.pre_project_311.service.UserService;
import com.example.pre_project_311.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));

        return "show";
    }

    @GetMapping("/new")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());

        return "new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user, @RequestParam("listRoles[]") String[] listRoles) {
        Set<Role> set = new HashSet<>();

        for (String s : listRoles) {
            set.add(roleService.getRoleByName(s));
        }

        user.setRoles(set);
        userService.addUser(user);

        return "redirect:/admin/";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") long id) {
        User user = userService.getUserById(id);
        user.setPassword("");
        model.addAttribute("user", user);

        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id,
                         @RequestParam("listRoles[]") String[] listRoles) {
        Set<Role> set = new HashSet<>();

        for (String s : listRoles) {
            set.add(roleService.getRoleByName(s));
        }

        user.setRoles(set);
        userService.updateUser(id, user);

        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);

        return "redirect:/admin/";
    }

}
