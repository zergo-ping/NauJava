package ru.Nikita.NauJava.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.UserRepository;

/**
 * Контроллер для отображения HTML страницы со списком пользователей.
 * Использует Thymeleaf для рендеринга шаблонов.
 */
@Controller
@RequestMapping("/custom/users/view")
public class UserView {

    private final UserRepository userController;


    @Autowired
    public UserView(UserRepository userController) {
        this.userController = userController;
    }


    @Transactional
    @GetMapping("/list")
    public String userListView(Model model)
    {
        Iterable<UserEntity> products = userController.findAll();
        model.addAttribute("users", products);
        return "userList";
    }
}
