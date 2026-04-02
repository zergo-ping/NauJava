package ru.Nikita.NauJava.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис для управления пользователями приложения.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Получить пользователя по электронной почте
     * 
     * @param email электронная почта пользователя
     * @return пользователь если найден
     */
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Добавить нового пользователя в систему
     * 
     * @param user объект пользователя
     * @return сохраненный пользователь
     */
    public UserEntity addUser(UserEntity user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }

    /**
     * Проверить существование пользователя по email
     * 
     * @param email электронная почта
     * @return true если пользователь существует
     */
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
