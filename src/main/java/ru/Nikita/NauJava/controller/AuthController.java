package ru.Nikita.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.transaction.RegistrationDto;
import ru.Nikita.NauJava.transaction.UserService;

/**
 * Контроллер для управления регистрацией и логированием пользователей
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отобразить страницу логина
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Отобразить форму регистрации
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return "register";
    }

    /**
     * Обработать форму регистрации
     */
    @PostMapping("/register")
    public String processRegistration(RegistrationDto registrationDto, Model model) {
        // Проверить, существует ли уже пользователь с этой почтой
        if (userService.userExists(registrationDto.getEmail())) {
            model.addAttribute("error", "Пользователь с такой почтой уже зарегистрирован");
            model.addAttribute("user", registrationDto);
            return "register";
        }

        // Проверить, совпадают ли пароли
        if (!registrationDto.getPassword().equals(registrationDto.getPasswordConfirm())) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("user", registrationDto);
            return "register";
        }

        // Создать нового пользователя
        UserEntity user = new UserEntity();
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(registrationDto.getPassword());
        user.setFullName(registrationDto.getFullName());
        user.setRole("USER");

        try {
            userService.addUser(user);
            return "redirect:/auth/login?success";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            model.addAttribute("user", registrationDto);
            return "register";
        }
    }




}
