package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.Nikita.NauJava.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями
 * 
 * Предоставляет методы для поиска и управления пользователями по критериям
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    /**
     * Поиск пользователя по электронной почте
     * 
     * @param email электронная почта пользователя
     * @return пользователь с указанной почтой, или null если не найден
     */
    UserEntity findByEmail(String email);

    /**
     * Проверка наличия пользователя с указанной электронной почтой
     * 
     * @param email электронная почта для проверки
     * @return true, если пользователь существует; false в противном случае
     */
    boolean existsByEmail(String email);



}