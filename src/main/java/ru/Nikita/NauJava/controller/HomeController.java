package ru.Nikita.NauJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для отображения главной страницы
 */
@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }
}
