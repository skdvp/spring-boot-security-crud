package com.skdvp.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.skdvp.app.model.User;
import com.skdvp.app.service.RoleService;
import com.skdvp.app.service.UserService;

@Controller
@RequestMapping("")
public class UserController {


    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/user")
    public String getUserPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("key", user);
        return "user";
    }

    /*==========================CRUD=================================*/


    @GetMapping(value = "/admin")
    public String showAllUsers(Model model) {
        // GET /user_list всех людей из DAO и передаём на отображение в представление
        model.addAttribute("key", userService.showAllUsers());
        return "user_list";
    }

    @GetMapping(value = "/user_list/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        // GET /user_list/:id получим одного человека по id из DAO и передаём на отображение в представление
        model.addAttribute("key", userService.showUser(id).get());
        return "show_user";
    }

    @GetMapping(value = "/user_list/new")
    public String newUser(Model model) {
        // GET /user_list/new запрос HTML формы для создания записи

        model.addAttribute("user", new User());
        model.addAttribute("roles_list", roleService.getAllRoles());
        return "new_user";
    }

    @PostMapping(value = "/admin/saveUser")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(value = "rolesIdSelect") Long[] idRole) {
        // POST /user_list создаём новую запись
        userService.saveUser(user, idRole);
        return "redirect:/admin";
    }

    @GetMapping(value = "/user_list/{id}/edit")
    // GET /user_list/:id/edit HTML форма редактирование записи
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.showUser(id).get());
        model.addAttribute("roles_list", roleService.getAllRoles());

        return "edit";
    }

    @PostMapping(value = "/update-user")
    // PATCH /user_list/:id HTML форма обновления записи
    public String updateUser(@ModelAttribute("user") User updateUser) {
        userService.updateSaveUser(updateUser);
        return "redirect:/admin";
    }

    @PostMapping(value = "/user_list/{id}/delete")
    // DELETE user_list/:id HTML форма удаления записи
    public String delete(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
