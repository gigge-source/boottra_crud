package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Список всех пользователей
    @GetMapping
    public ModelAndView getAllUsers(Principal principal) {
        User currentUser = userService.userByEmail(principal.getName());
        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("users")
                .addObject("currentUser", currentUser.getEmail())
                .addObject("users", userService.getAllUsers());
    }

    // Добавление пользователя (обработка формы)
    @PostMapping("/add")
    public ModelAndView addUsers(@ModelAttribute("user") User user,
                                 @RequestParam(value = "roleIds", required = false) Set<Long> roleIds) {
        userService.saveUser(user, roleIds);
        return new ModelAndView("redirect:/admin");
    }

    // Удаление пользователя
    @PostMapping("/delete")
    public ModelAndView deleteUsers(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
    }

    // Обновление пользователя
    @PostMapping("/update")
    public ModelAndView updateUsers(@ModelAttribute("user") User user,
                                    @RequestParam(value = "roleIds", required = false) Set<Long> roleIds) {
        userService.updateUser(user, roleIds);
        return new ModelAndView("redirect:/admin");
    }

    // Форма добавления пользователя
    @GetMapping("/add-user")
    public ModelAndView addUserForm(Principal principal) {
        User currentUser = userService.userByEmail(principal.getName());
        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("add-user")
                .addObject("currentUser", currentUser.getEmail())
                .addObject("user", new User());
        // если нужны роли: .addObject("roles", roleService.getAllRoles())
    }

    // Форма редактирования пользователя
    @GetMapping("/edit-user")
    public ModelAndView editUserForm(@RequestParam("id") Long id, Principal principal) {
        User currentUser = userService.userByEmail(principal.getName());
        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("edit")
                .addObject("currentUser", currentUser.getEmail())
                .addObject("user", userService.findUserById(id));
    }

    // Форма подтверждения удаления
    @GetMapping("/delete-user")
    public ModelAndView deleteUserForm(@RequestParam("id") Long id, Principal principal) {
        User currentUser = userService.userByEmail(principal.getName());
        if (currentUser == null) {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("delete")
                .addObject("currentUser", currentUser.getEmail())
                .addObject("user", userService.findUserById(id));
    }
}