package ru.Nikita.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.Nikita.NauJava.entity.StorageQuotaEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.QuotaRepository;
import ru.Nikita.NauJava.transaction.UserService;

/**
 * Инициализатор данных по умолчанию при запуске приложения.
 * Создаёт учётные записи администратора и обычного пользователя, если они отсутствуют.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final QuotaRepository quotaRepository;

    @Autowired
    public DataInitializer(UserService userService, QuotaRepository quotaRepository) {
        this.userService = userService;
        this.quotaRepository = quotaRepository;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin@example.com", "admin", "Admin", "ADMIN");
        createUserIfNotExists("user@example.com", "user", "User", "USER");
    }

    private void createUserIfNotExists(String email, String password, String fullName, String role) {
        if (userService.userExists(email)) {
            return;
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setFullName(fullName);
        user.setRole(role);

        UserEntity savedUser = userService.addUser(user);

        StorageQuotaEntity quota = new StorageQuotaEntity();
        quota.setUser(savedUser);
        quota.setMaxBytes(1073741824L);
        quota.setUsedBytes(0L);
        quotaRepository.save(quota);
    }
}
