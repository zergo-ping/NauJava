package ru.Nikita.NauJava.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Nikita.NauJava.repository.UserRepository;


/**
 * REST контроллер для управления пользователями.
 * Предоставляет эндпоинты для получения информации о пользователях.
 */
@RestController
@RequestMapping("/custom/users")
public class UserController {

    private final UserRepository userRepository;


    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    @GetMapping("/findAll")
    public String findAll()
    {
        return userRepository.findAll().toString();
    }
}
